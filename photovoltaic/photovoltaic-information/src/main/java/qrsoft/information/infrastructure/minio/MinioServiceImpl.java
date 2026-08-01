package qrsoft.information.infrastructure.minio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import qrsoft.information.infrastructure.minio.IMinioService;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class MinioServiceImpl implements IMinioService {

	@Value("${local.upload-dir:./uploads}")
	private String uploadDir;

	@Override
	public String upload(MultipartFile file) throws Exception {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("文件不能为空");
		}
		String original = file.getOriginalFilename();
		String ext = ".jpg";
		if (StringUtils.hasText(original) && original.contains(".")) {
			ext = original.substring(original.lastIndexOf('.')).toLowerCase();
		}
		String key = "station/" + UUID.randomUUID().toString().replace("-", "") + ext;
		Path dir = baseDir();
		Path target = dir.resolve(key).normalize();
		if (!target.startsWith(dir)) {
			throw new IllegalArgumentException("非法路径");
		}
		Files.createDirectories(target.getParent());
		file.transferTo(target.toFile());
		return key;
	}

	@Override
	public Path resolveLocalPath(String relativeKey) {
		if (!StringUtils.hasText(relativeKey)) {
			return null;
		}
		String key = relativeKey;
		if (key.startsWith("api/")) {
			key = key.substring(4);
		}
		// 去掉前导 /
		while (key.startsWith("/")) {
			key = key.substring(1);
		}
		Path dir = baseDir();
		Path file = dir.resolve(key).normalize();
		if (!file.startsWith(dir) || !Files.isRegularFile(file)) {
			return null;
		}
		return file;
	}

	@Override
	public InputStream openStream(String relativeKey) throws Exception {
		Path file = resolveLocalPath(relativeKey);
		if (file == null) {
			throw new IllegalArgumentException("文件不存在");
		}
		return Files.newInputStream(file);
	}

	@Override
	public String probeContentType(String relativeKey) throws Exception {
		Path file = resolveLocalPath(relativeKey);
		if (file == null) {
			return null;
		}
		return Files.probeContentType(file);
	}

	private Path baseDir() {
		return Paths.get(uploadDir).toAbsolutePath().normalize();
	}
}
