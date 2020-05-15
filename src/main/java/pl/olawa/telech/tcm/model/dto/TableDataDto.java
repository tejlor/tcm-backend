package pl.olawa.telech.tcm.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.olawa.telech.tcm.model.interfaces.Loggable;
import pl.olawa.telech.tcm.model.shared.TableParams;

@Getter @Setter
public class TableDataDto<T> implements Loggable {

	private TableParams tableParams;
	private TableInfoDto tableInfo;
	private List<T> rows;
	
	
	public TableDataDto(TableParams tableParams) {
		this.tableParams = tableParams;
		tableInfo = new TableInfoDto();
	}
	
	public void setCount(int count) {
		tableInfo.setCount(tableParams, count);
	}
}

@Getter @Setter
class TableInfoDto {
	private int pageCount;
	private int rowCount;
	private int rowStart;
	private int rowEnd;
	
	public void setCount(TableParams tableParams, int count) {
		rowCount = count;
		pageCount = (int) Math.ceil((double)count / tableParams.getPageSize());
		rowStart = tableParams.getPageNo() * tableParams.getPageSize() + 1;
		rowEnd = Math.min(rowStart + tableParams.getPageSize() - 1, count);
	}
}
