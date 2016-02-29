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
			return "TopNewsData [id=" + id + ", topimage=" + topimage + ", pubdate=" + pubdate + ", title=" + title
					+ ", type=" + type + ", url=" + url + "]";
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
			return "TabNewsData [id=" + id + ", listimage=" + listimage + ", pubdate=" + pubdate + ", title=" + title
					+ ", type=" + type + ", url=" + url + "]";
		}

	}
}
