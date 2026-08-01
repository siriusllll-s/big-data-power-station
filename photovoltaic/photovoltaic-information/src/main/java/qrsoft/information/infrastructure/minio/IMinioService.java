package qrsoft.information.infrastructure.minio;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * 文件上传下载（实验环境本地落盘，接口名兼容 MinIO）
 */
public interface IMinioService {

	/**
	 * 上传文件，返回存储 key（相对路径）
	 */
	String upload(MultipartFile file) throws Exception;

	/**
	 * 解析预览用的本地绝对路径；非法或不存在返回 null
	 */
	Path resolveLocalPath(String relativeKey);

	/**
	 * 打开文件流
	 */
	InputStream openStream(String relativeKey) throws Exception;

	/**
	 * 猜测 Content-Type
	 */
	String probeContentType(String relativeKey) throws Exception;
}
