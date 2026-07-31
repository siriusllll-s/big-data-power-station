package qrsoft.information.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.common.entity.Experience;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.mapper.ExperienceMapper;

import java.util.*;

@RestController
@RequestMapping("/experience")
public class ExperienceController {

	@Autowired private ExperienceMapper experienceMapper;

	@PostMapping("/pageByParam")
	public WrappedResult<ResultPage<Map<String, Object>>> page(@RequestBody Map<String, Object> body) {
		int pageNo = body.get("page") == null ? 1 : Integer.parseInt(String.valueOf(body.get("page")));
		int limit = body.get("limit") == null ? 10 : Integer.parseInt(String.valueOf(body.get("limit")));
		String keyword = body.get("keyword") == null ? null : String.valueOf(body.get("keyword"));
		QueryWrapper<Experience> q = new QueryWrapper<>();
		q.eq("del_flag", 0);
		if (StringUtils.isNotBlank(keyword)) {
			q.and(w -> w.like("title", keyword).or().like("content", keyword));
		}
		q.orderByDesc("id");
		Page<Experience> page = experienceMapper.selectPage(new Page<>(pageNo, limit), q);
		ResultPage<Map<String, Object>> result = new ResultPage<>(page);
		List<Map<String, Object>> list = new ArrayList<>();
		for (Experience e : page.getRecords()) {
			Map<String, Object> m = new HashMap<>();
			m.put("id", e.getId());
			m.put("title", e.getTitle());
			m.put("deviceType", e.getDeviceType());
			m.put("content", e.getContent());
			m.put("createTime", e.getCreateTime() == null ? null : DateUtil.dateToString(e.getCreateTime(), DateUtil.YYMMDD_HHMMSS));
			list.add(m);
		}
		result.setList(list);
		return WrappedResult.successWrappedResult(result);
	}

	@PostMapping("/save")
	public WrappedResult<Boolean> save(@RequestBody Experience input) {
		Date now = new Date();
		if (input.getId() == null) {
			input.setCreateTime(now);
			input.setUpdateTime(now);
			input.setDelFlag(0);
			experienceMapper.insert(input);
		} else {
			Experience old = experienceMapper.selectById(input.getId());
			if (old == null) return WrappedResult.failedWrappedResult("记录不存在");
			old.setTitle(input.getTitle());
			old.setDeviceType(input.getDeviceType());
			old.setContent(input.getContent());
			old.setUpdateTime(now);
			experienceMapper.updateById(old);
		}
		return WrappedResult.successWrappedResult(true);
	}

	@GetMapping("/delete/{id}")
	public WrappedResult<Boolean> delete(@PathVariable Integer id) {
		Experience e = experienceMapper.selectById(id);
		if (e == null) return WrappedResult.failedWrappedResult("记录不存在");
		e.setDelFlag(1);
		experienceMapper.updateById(e);
		return WrappedResult.successWrappedResult(true);
	}
}
