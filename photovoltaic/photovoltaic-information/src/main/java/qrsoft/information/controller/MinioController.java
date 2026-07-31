package qrsoft.information.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.service.IMinioService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * 文件上传下载操作类（委托 IMinioService，实验环境本地存储）
 */
@RestController
@RequestMapping("/minio")
@Api(tags = "文件上传")
public class MinioController {

	@Autowired
	private IMinioService minioService;

	@PostMapping("/upload")
	@ApiOperation("上传图片")
	public WrappedResult<?> upload(@RequestParam(value = "file", required = false) MultipartFile file,
								   @RequestParam(value = "upload", required = false) MultipartFile upload) {
		try {
			MultipartFile mf = file != null ? file : upload;
			String key = minioService.upload(mf);
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
			Path file = minioService.resolveLocalPath(relative);
			if (file == null) {
				response.setStatus(404);
				return;
			}
			String contentType = minioService.probeContentType(relative);
			response.setContentType(StringUtils.hasText(contentType) ? contentType : MediaType.IMAGE_JPEG_VALUE);
			try (InputStream in = minioService.openStream(relative); OutputStream out = response.getOutputStream()) {
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
