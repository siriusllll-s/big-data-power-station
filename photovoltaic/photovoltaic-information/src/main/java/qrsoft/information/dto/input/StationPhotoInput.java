package qrsoft.information.dto.input;

import lombok.Data;

@Data
public class StationPhotoInput {
	/** 电站 id（后端主字段） */
	private Integer station;
	/** 兼容前端 UploadImage 传 id */
	private Integer id;
	/** 图片路径（后端主字段） */
	private String photo;
	/** 兼容前端 photoPath */
	private String photoPath;

	public Integer resolveStationId() {
		return station != null ? station : id;
	}

	public String resolvePhoto() {
		if (photo != null && !photo.isEmpty()) {
			return photo;
		}
		return photoPath;
	}
}
