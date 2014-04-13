package com.example.spaceapps;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	LinearLayout ll;

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	public ViewPager mViewPager;
	WebView mWebView;
	private static View view;
	static int p = 0;

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this,
				AlertDialog.THEME_HOLO_DARK);

		alert.setTitle("Exit");
		alert.setIcon(android.R.drawable.ic_lock_power_off);
		alert.setMessage("Do you want to exit application?");
		alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				moveTaskToBack(true);
			}
		});

		alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = alert.create();
		dialog.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();

		actionBar.setHomeButtonEnabled(false);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
						Log.d("dhvhfj", position + "");
						p = position;
					}
				});

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));

		}

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return new LaunchpadSectionFragment();

			default:

				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();

				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;

			}
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String[] ArrayNames = { "°C Map", "Co2", "Anomalies", "CH4" };
			return ArrayNames[position];
		}
	}

	public static class LaunchpadSectionFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			if (view != null) {
				ViewGroup parent = (ViewGroup) view.getParent();
				if (parent != null)
					parent.removeView(view);

			}
			try {
				view = inflater.inflate(R.layout.fragment_include, container,
						false);

				WebView webView = (WebView) view.findViewById(R.id.webview);

				webView.setInitialScale(1);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setUseWideViewPort(true);
				webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
				webView.setScrollbarFadingEnabled(false);
				String url = "file:///android_asset/heat1.html";
				webView.loadUrl(url);

				webView.setWebViewClient(new WebViewClient() {
					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
						view.loadUrl(url);
						return true;
					}
				});

			} catch (InflateException e) {

			}
			return view;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			setUserVisibleHint(true);
		}
	}

	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			if (container == null) {
				return null;
			}

			GraphicalView mChart;

			int[] years = { 1880, 1881, 1882, 1883, 1884, 1885, 1886, 1887,
					1888, 1889, 1890, 1891, 1892, 1893, 1894, 1895, 1896, 1897,
					1898, 1899, 1900, 1901, 1902, 1903, 1904, 1905, 1906, 1907,
					1908, 1909, 1910, 1911, 1912, 1913, 1914, 1915, 1916, 1917,
					1918, 1919, 1920, 1921, 1922, 1923, 1924, 1925, 1926, 1927,
					1928, 1929, 1930, 1931, 1932, 1933, 1934, 1935, 1936, 1937,
					1938, 1939, 1940, 1941, 1942, 1943, 1944, 1945, 1946, 1947,
					1948, 1949, 1950, 1951, 1952, 1953, 1954, 1955, 1956, 1957,
					1958, 1959, 1960, 1961, 1962, 1963, 1964, 1965, 1966, 1967,
					1968, 1969, 1970, 1971, 1972, 1973, 1974, 1975, 1976, 1977,
					1978, 1979, 1980, 1981, 1982, 1983, 1984, 1985, 1986, 1987,
					1988, 1989, 1990, 1991, 1992, 1993, 1994, 1995, 1996, 1997,
					1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007,
					2008, 2009, 2010, 2011, 2012, 2013 };

			double[] value = { -0.16, -0.1, -0.12, -0.17, -0.23, -0.2, -0.18,
					-0.26, -0.17, -0.09, -0.3, -0.26, -0.3, -0.34, -0.3, -0.25,
					-0.1, -0.15, -0.28, -0.16, -0.11, -0.17, -0.26, -0.35,
					-0.41, -0.29, -0.24, -0.38, -0.43, -0.43, -0.42, -0.44,
					-0.38, -0.35, -0.19, -0.11, -0.31, -0.34, -0.24, -0.26,
					-0.24, -0.16, -0.25, -0.23, -0.22, -0.15, -0.06, -0.13,
					-0.13, -0.24, -0.06, -0.03, -0.05, -0.2, -0.05, -0.08,
					-0.05, 0.06, 0.08, 0.07, 0.11, 0.16, 0.12, 0.11, 0.22,
					0.09, -0.05, -0.05, -0.06, -0.07, -0.16, -0.0, 0.04, 0.12,
					-0.1, -0.11, -0.17, 0.06, 0.12, 0.07, 0.02, 0.1, 0.12,
					0.14, -0.12, -0.06, 0.0, 0.02, 0.01, 0.11, 0.07, -0.03,
					0.05, 0.19, -0.05, 0.02, -0.08, 0.18, 0.1, 0.18, 0.23,
					0.27, 0.15, 0.32, 0.13, 0.11, 0.2, 0.33, 0.34, 0.27, 0.4,
					0.38, 0.24, 0.26, 0.33, 0.45, 0.32, 0.52, 0.64, 0.46, 0.43,
					0.55, 0.61, 0.62, 0.58, 0.65, 0.6, 0.59, 0.51, 0.6, 0.66,
					0.53, 0.58, 0.62 };

			int z[] = new int[years.length];
			double x[] = new double[years.length];
			int k = 1;

			for (int i = 0; i < years.length; i++) {
				z[i] = k++;
				x[i] = value[i];
			}

			XYSeries xSeries = new XYSeries("X Series");

			for (int i = 0; i < z.length; i++) {
				xSeries.add(z[i], x[i]);

			}

			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

			dataset.addSeries(xSeries);

			XYSeriesRenderer Xrenderer = new XYSeriesRenderer();
			Xrenderer.setColor(Color.GREEN);
			Xrenderer.setPointStyle(PointStyle.DIAMOND);
			Xrenderer.setDisplayChartValues(true);
			Xrenderer.setLineWidth(5);
			Xrenderer.setFillPoints(true);

			XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

			mRenderer.setChartTitle("Climate Anomalities");
			mRenderer.setXTitle("Years");
			mRenderer.setYTitle("Anomalities");
			mRenderer.setZoomButtonsVisible(true);
			mRenderer.setZoomEnabled(true);
			mRenderer.setXLabels(0);
			mRenderer.setPanEnabled(false);

			mRenderer.setShowGrid(true);

			mRenderer.setClickEnabled(true);

			for (int i = 0; i < z.length; i++) {
				mRenderer.addXTextLabel(i, years[i] + "");
			}

			mRenderer.addSeriesRenderer(Xrenderer);
			mChart = ChartFactory.getLineChartView(getActivity(), dataset,
					mRenderer);
			View view = (LinearLayout) inflater.inflate(
					R.layout.fragment_section_dummy, container, false);
			LinearLayout chart_container = (LinearLayout) view
					.findViewById(R.id.chart);
			chart_container.addView(mChart);

			return view;
		}
	}

}
