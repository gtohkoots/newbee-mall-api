package ltd.newbee.mall.newbeemallapi.util;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class PageResult<T> {
	
    @ApiModelProperty("总记录数")
    private int totalCount;

    @ApiModelProperty("每页记录数")
    private int pageSize;

    @ApiModelProperty("总页数")
    private int totalPage;

    @ApiModelProperty("当前页数")
    private int currPage;

    @ApiModelProperty("列表数据")
    private List<T> list;

	public PageResult(int totalCount, int pageSize, int currPage, List<T> list) {
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
