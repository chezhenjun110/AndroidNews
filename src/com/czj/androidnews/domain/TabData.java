package com.czj.androidnews.domain;

import java.util.ArrayList;

public class TabData {
	public int retcode;
	public TabDetail data;

	public class TabDetail {
		public String title;
		public String more;
		public ArrayList<TabNewsData> news;
		public ArrayList<TopNewsData> topnews;
		@Override
		public String toString() {
			return "TabDetail [title=" + title + ", more=" + more + ", news=" + news + ", topnews=" + topnews + "]";
		}

		

	}

	public class TopNewsData {
		public String id;
		public String topimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
		@Override
		public String toString() {
			return "TopNewsData [topimage=" + topimage + ", title=" + title + "]";
		}
		

		

	}

	public class TabNewsData {
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
		@Override
		public String toString() {
			return "TabNewsData [id=" + id + ", title=" + title + "]";
		}

		

	}

	@Override
	public String toString() {
		return "TabData [retcode=" + retcode + ", data=" + data + "]";
	}
	
}
