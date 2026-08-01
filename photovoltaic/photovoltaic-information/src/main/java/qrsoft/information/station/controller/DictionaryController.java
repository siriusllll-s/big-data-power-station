package qrsoft.information.station.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qrsoft.common.entity.Area;
import qrsoft.common.entity.City;
import qrsoft.common.entity.Province;
import qrsoft.information.dto.output.AreaOutput;
import qrsoft.information.dto.output.CityOutput;
import qrsoft.information.dto.output.ProvinceOutput;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.mapper.AreaMapper;
import qrsoft.information.mapper.CityMapper;
import qrsoft.information.mapper.ProvinceMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dictionary")
@Api(tags = "字典/省市区")
public class DictionaryController {

	@Autowired
	private ProvinceMapper provinceMapper;
	@Autowired
	private CityMapper cityMapper;
	@Autowired
	private AreaMapper areaMapper;

	@GetMapping("/province")
	@ApiOperation("省份列表")
	public WrappedResult<List<ProvinceOutput>> provinceList() {
		List<Province> list = provinceMapper.selectList(null);
		List<ProvinceOutput> outputs = list.stream().map(ProvinceOutput::entityToOutput).collect(Collectors.toList());
		return WrappedResult.successWrappedResult(outputs);
	}

	@GetMapping("/city/{provinceId}")
	@ApiOperation("城市列表")
	public WrappedResult<List<CityOutput>> cityList(
			@ApiParam(value = "省份 id", required = true) @PathVariable Integer provinceId) {
		List<City> list = cityMapper.cityList(provinceId);
		List<CityOutput> outputs = list.stream().map(CityOutput::entityToOutput).collect(Collectors.toList());
		return WrappedResult.successWrappedResult(outputs);
	}

	@GetMapping("/area/{cityId}")
	@ApiOperation("区县列表")
	public WrappedResult<List<AreaOutput>> areaList(
			@ApiParam(value = "城市 id", required = true) @PathVariable Integer cityId) {
		List<Area> list = areaMapper.areaList(cityId);
		List<AreaOutput> outputs = list.stream().map(AreaOutput::entityToOutput).collect(Collectors.toList());
		return WrappedResult.successWrappedResult(outputs);
	}
}
