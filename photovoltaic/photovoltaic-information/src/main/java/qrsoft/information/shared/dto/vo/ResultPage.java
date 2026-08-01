package qrsoft.information.shared.dto.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultPage<T> {
	private long total;
	private long page;
	private long limit;
	private List<T> list = new ArrayList<>();

	public ResultPage() {
	}

	public ResultPage(Page<?> page) {
		this.total = page.getTotal();
		this.page = page.getCurrent();
		this.limit = page.getSize();
	}
}
