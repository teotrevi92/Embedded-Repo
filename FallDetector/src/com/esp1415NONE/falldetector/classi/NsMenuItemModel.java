package com.esp1415NONE.falldetector.classi;

public class NsMenuItemModel {

	public int title;
	public int iconRes;
	public boolean isHeader;

	public NsMenuItemModel(int title, int iconRes,boolean header) {
		this.title = title;
		this.iconRes = iconRes;
		this.isHeader=header;
	}

	public NsMenuItemModel(int title, int iconRes) {
		this(title,iconRes,false);
	}

}
