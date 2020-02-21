package pl.olawa.telech.tcm.model.shared;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TableParams {

	private int pageNo;
	private int pageSize;
	private String filter;
	private String sortBy;
	private boolean sortAsc;
	
	public TableParams() {
		pageNo = 0;
		pageSize = 10;
		sortBy = "name";
		sortAsc = true;
	}
	
	public TableParams(Integer pageNo, Integer pageSize, String filter, String sortBy, Boolean sortAsc) {
		this();

		if(pageNo != null)
			this.pageNo = pageNo;
		
		if(pageSize != null)
			this.pageSize = pageSize;
		
		if(filter != null)
			this.filter = filter;
		
		if(sortBy != null && sortBy.length() > 0)
			this.sortBy = sortBy;
		
		if(sortAsc != null)
			this.sortAsc = sortAsc;	
	}
	
	public Sort getSort() {
		return Sort.by(sortAsc ? Direction.ASC : Direction.DESC, sortBy);
	}
	
	public Pageable getPage() {
		return PageRequest.of(pageNo, pageSize, getSort());
	}
}
