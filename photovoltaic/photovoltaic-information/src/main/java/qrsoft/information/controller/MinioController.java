package qrsoft.information.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qrsoft.information.dto.vo.WrappedResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 实验环境本地文件上传，路径兼容前端 /minio/upload 与 /minio/preViewPicture/**。
 */
@RestController
@RequestMapping("/minio")
@Api(tags = "文件上传")
public class MinioController {

	@Value("${local.upload-dir:./uploads}")
	private String uploadDir;

	@PostMapping("/upload")
	@ApiOperation("上传图片")
	public WrappedResult<?> upload(@RequestParam(value = "file", required = false) MultipartFile file,
								   @RequestParam(value = "upload", required = false) MultipartFile upload) {
		try {
			MultipartFile mf = file != null ? file : upload;
			if (mf == null || mf.isEmpty()) {
				return WrappedResult.failedWrappedResult("文件不能为空");
			}
			String original = mf.getOriginalFilename();
			String ext = ".jpg";
			if (StringUtils.hasText(original) && original.contains(".")) {
				ext = original.substring(original.lastIndexOf('.')).toLowerCase();
			}
			String key = "station/" + UUID.randomUUID().toString().replace("-", "") + ext;
			Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
			Path target = dir.resolve(key).normalize();
			if (!target.startsWith(dir)) {
				return WrappedResult.failedWrappedResult("非法路径");
			}
			Files.createDirectories(target.getParent());
			mf.transferTo(target.toFile());
			Map<String, String> result = new HashMap<>();
			result.put("key", key);
			result.put("path", key);
			result.put("name", key);
			result.put("url", key);
			// 前端 handleSuccess 会取 resultValue 或 string
			return WrappedResult.successWrappedResult(key);
		} catch (Exception e) {
			return WrappedResult.failedWrappedResult("上传失败: " + e.getMessage());
		}
	}

	@GetMapping("/preViewPicture/**")
	@ApiOperation("预览图片")
	public void preview(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uri = request.getRequestURI();
			String marker = "/preViewPicture/";
			int idx = uri.indexOf(marker);
			if (idx < 0) {
				response.setStatus(404);
				return;
			}
			String relative = uri.substring(idx + marker.length());
			// 兼容 /api/minio/... 代理后的路径
			if (relative.startsWith("api/")) {
				relative = relative.substring(4);
			}
			if (!StringUtils.hasText(relative)) {
				response.setStatus(404);
				return;
			}
			Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
			Path file = dir.resolve(relative).normalize();
			if (!file.startsWith(dir) || !Files.isRegularFile(file)) {
				response.setStatus(404);
				return;
			}
			String contentType = Files.probeContentType(file);
			response.setContentType(contentType != null ? contentType : MediaType.IMAGE_JPEG_VALUE);
			try (InputStream in = Files.newInputStream(file); OutputStream out = response.getOutputStream()) {
				byte[] buf = new byte[8192];
				int n;
				while ((n = in.read(buf)) > 0) {
					out.write(buf, 0, n);
				}
			}
		} catch (Exception e) {
			response.setStatus(500);
		}
	}
}
