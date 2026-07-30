package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("station")
public class Station {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String no;
	private String name;
	private String shortName;
	private Double installCapacity;
	private Integer status;
	private Integer type;
	private Double lon;
	private Double lat;
	private Integer maintainPerson;
	private Integer stationPerson;
	private String constructionPerson;
	private String designPerson;
	private String own;
	private Integer provinceId;
	private Integer cityId;
	private Integer areaId;
	private String address;
	private Integer saleType;
	private Double contractPower;
	private Date contractTime;
	private Double protocolPr;
	private Date netTime;
	private Double avgRadio;
	private Double efficiency;
	private String stationDesc;
	private String componentInfo;
	private Integer buildCycle;
	private Integer joinLevel;
	private Integer delFlag;
	private String photoPath;
	private Double totalInPower;
	private Double totalOutPower;
	private Double averageEfficiency;
}
