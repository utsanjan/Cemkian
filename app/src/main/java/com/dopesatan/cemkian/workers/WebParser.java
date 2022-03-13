package com.dopesatan.cemkian.workers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;

import com.dopesatan.cemkian.attendance_objs.AttendanceData;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebParser {
	
	private static final String login_link = "https://www.cemkolaghat.org/student/login.aspx";
	private static final String att_link = "https://www.cemkolaghat.org/student/student-attendance-report.aspx";
	private static final String index_link = "https://cemkolaghat.org/student/index.aspx";
	private static final String[] paramValues = new String[6];
	private static String COOKIE = "?";
	private OkHttpClient client;
	private Runner runner;
	
	@SuppressWarnings("SameParameterValue")
	public void start(String username, String password, Runner runner_) throws Exception {
		client = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).build();
		paramValues[3] = username;
		paramValues[4] = password;
		this.runner = runner_;
		startInternal();
	}
	
	private void startInternal() throws Exception {
		Request request = new Request.Builder()
				.url(login_link)
				.get()
				.build();
		Response response = null;
		try {
			response = client.newCall(request).execute();
		} catch (Exception e) {
			runner.onError();
			e.printStackTrace();
		}
		if (response != null) {
			if (response.body() != null) {
				obtainCookieAndGo(Objects.requireNonNull(response.body()).string());
			} else {
				runner.onError();
			}
		} else {
			runner.onError();
		}
	}
	
	private void obtainCookieAndGo(String response) throws Exception {
		try {
			Document doc = Jsoup.parse(response);
			paramValues[0] = doc.select("input").first().val();
			paramValues[1] = doc.select("input").get(1).val();
			paramValues[2] = doc.select("input").get(2).val();
			paramValues[5] = "Login"; //What the hell?
		} catch (Exception e) {
			runner.onError();
		}
		getCookieAndGo();
	}
	
	private void getCookieAndGo() throws Exception {
		RequestBody formBody = new FormBody.Builder()
				.add(Params.one.val, paramValues[0])
				.add(Params.two.val, paramValues[1])
				.add(Params.three.val, paramValues[2])
				.add(Params.four.val, paramValues[3])
				.add(Params.five.val, paramValues[4])
				.add(Params.six.val, paramValues[5])
				.build();
		
		Request request = new Request.Builder()
				.url(login_link)
				.post(formBody)
				.header("Host", "www.cemkolaghat.org")
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
				.header("Accept-Language", "en-US,en;q=0.5")
				.header("Accept-Encoding", "gzip,deflate")
				.header("Referer", login_link)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("Content-Length", String.valueOf(
						paramValues[0].length() + paramValues[1].length() + paramValues[2].length()
								+ paramValues[3].length() + paramValues[4].length() + paramValues[5].length()
				))
				.header("Connection", "close")
				.header("Upgrade-Insecure-Requests", "1")
				.build();
		
		Response response = null;
		try {
			response = client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
			runner.onError();
		}
		
		if (response != null) {
			COOKIE = response.header("Set-Cookie");
		} else {
			runner.onError();
		}
		runner.onCheckComplete(COOKIE == null);
		fetchAttendanceResult();
	}
	
	private void fetchAttendanceResult() throws Exception {
		Request request = new Request.Builder()
				.url(att_link)
				.get()
				.header("Host", "www.cemkolaghat.org")
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
				.header("Accept-Language", "en-US,en;q=0.5")
				.header("Referer", index_link)
				.header("Connection", "close")
				.header("Cookie", COOKIE)
				.header("Upgrade-Insecure-Requests", "1")
				.build();
		Response response = null;
		try {
			response = client.newCall(request).execute();
		} catch (Exception e) {
			runner.onError();
			e.printStackTrace();
		}
		if (response != null) {
			if (response.body() != null) {
				processAttendanceResult(Objects.requireNonNull(response.body()).string());
			} else {
				runner.onError();
			}
		} else {
			runner.onError();
		}
	}
	
	private void processAttendanceResult(String string) {
		//	Logger.log(string);
		Document doc = null;
		try {
			doc = Jsoup.parse(string);
		} catch (Exception e) {
			e.printStackTrace();
			runner.onError();
		}
		Element table; //select the first table.
		Elements rows;
		if (doc != null) {
			table = doc.select("table").get(0);
			rows = table.select("tr");
		} else {
			runner.onError();
			return;
		}
		
		AttendanceData data = new AttendanceData();
		data.setCached(false);
		try {
			if (doc.select("span").get(1).text().equals("No Record Found")) {
				data.setAvailable(false);
				runner.onComplete(data);
				return;
			} else {
				data.setAvailable(true);
			}
		} catch (Exception e) {
			data.setAvailable(true);
		}
		Element rowSubsPre = rows.get(0);
		Elements rowSubsPost = rowSubsPre.select("th");
		for (int i = 1; i < rowSubsPost.size(); i++) {
			String heads = rowSubsPost.get(i).text();
			data.addSub(heads.contains("-") ? heads.split("-")[1].trim() : heads); // BS-M201 -> M201
		}
		
		String header;
		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");
			
			header = cols.get(0).text();
			
			for (int z = 1; z < cols.size(); z++) {
				switch (header) {
					case "Total Hours":
						data.addHourTotal(Integer.parseInt(cols.get(z).text()));
						break;
					case "Hours Attended":
						data.addHourObt(Integer.parseInt(cols.get(z).text()));
						break;
					case "Percentage":
						data.addPercent(Float.parseFloat(cols.get(z).text()));
						break;
				}
			}
		}
		runner.onComplete(data);
	}
	
	enum Params {
		one("__VIEWSTATE"),
		two("__VIEWSTATEGENERATOR"),
		three("__EVENTVALIDATION"),
		four("ctl00$ContentPlaceHolder1$txtuserId"),
		five("ctl00$ContentPlaceHolder1$txtpassword"),
		six("ctl00$ContentPlaceHolder1$btnlogin");
		
		private String val;
		
		Params(String par) {
			this.val = par;
		}
	}
	
}



