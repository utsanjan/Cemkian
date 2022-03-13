package com.dopesatan.cemkian.jobs;


import android.app.Application;
import com.evernote.android.job.JobManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.dopesatan.cemkian.workers.DTM;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class AppOnCemk extends Application {
	
	private final String resp = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
			"\n" +
			"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
			"<head>\n" +
			"    <meta charset=\"utf-8\">\n" +
			"    <title>College of Engineering and Management, Kolaghat</title>\n" +
			"    <link href=\"../images/favicon.png\" rel=\"shortcut icon\">\n" +
			"    <link href=\"../css/dashboard.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">\n" +
			"    <link href=\"../css/font-awesome.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">\n" +
			"    <link href=\"../css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">\n" +
			"    <link href=\"../css/menu.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">\n" +
			"    <script src=\"../js/organictabs.jquery.js\" type=\"text/javascript\"></script>\n" +
			"    <script src=\"../js/jquery.min.js\" type=\"text/javascript\"></script>\n" +
			"    <script type=\"text/javascript\" src=\"../js/script.js\"></script>\n" +
			"</head>\n" +
			"<body>\n" +
			"    <form method=\"post\" action=\"./student-attendance-report.aspx\" id=\"form1\">\n" +
			"<div class=\"aspNetHidden\">\n" +
			"<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwULLTE5MDg3NTYwODYPZBYCZg9kFgICAQ9kFgYCAQ8WAh4JaW5uZXJodG1sBZABPHA+PHN0cm9uZz5Vc2VybmFtZSA8L3N0cm9uZz4tIGl0MTgwMjwvcD48YSBocmVmPSIvc3R1ZGVudC9wYXNzd29yZC1jaGFuZ2UuYXNweCI+Q2hhbmdlIFBhc3N3b3JkPC9hPjxhIGhyZWY9Ii4uL3N0dWRlbnQvc2luZ291dC5hc3B4Ij5Mb2dvdXQ8L2E+ZAIDDxYCHwAFGTxoMT5XZWxjb21lIDogUFJBVElNPC9oMT5kAgUPZBYCAgMPPCsAEQIADxYEHgtfIURhdGFCb3VuZGceC18hSXRlbUNvdW50AgNkDBQrAAMWCB4ETmFtZQULU3ViamVjdENvZGUeCklzUmVhZE9ubHloHgRUeXBlGSsCHglEYXRhRmllbGQFC1N1YmplY3RDb2RlFggfAwUJUENDLUNTMzAyHwRoHwUZKwIfBgUJUENDLUNTMzAyFggfAwUFVG90YWwfBGgfBRkrAh8GBQVUb3RhbBYCZg9kFggCAQ9kFgZmDw8WAh4EVGV4dAULVG90YWwgSG91cnNkZAIBDw8WAh8HBQEzZGQCAg8PFgIfBwUBM2RkAgIPZBYGZg8PFgIfBwUOSG91cnMgQXR0ZW5kZWRkZAIBDw8WAh8HBQEyZGQCAg8PFgIfBwUBMmRkAgMPZBYGZg8PFgIfBwUKUGVyY2VudGFnZWRkAgEPDxYCHwcFBTY2LjY3ZGQCAg8PFgIfBwUFNjYuNjdkZAIEDw8WAh4HVmlzaWJsZWhkZBgBBTBjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJGdkdnN0ZGF0dGVuZGFuY2VyZXBvcnQPPCsADAEIAgFkVXxk91ehUVGh+LFMbjHswjN+FYc=\" />\n" +
			"</div>\n" +
			"\n" +
			"<div class=\"aspNetHidden\">\n" +
			"\n" +
			"\t<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"14AC890E\" />\n" +
			"\t<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"/wEdAAOIYs5HFyYtgTRwzb+snYLWfUtnYvHEbQLM5cfxi/3JubxC9hCBpdqJAg9QFX1LiNpHAfsh4eJ0/pgXHnDVJL24G6uFOQ==\" />\n" +
			"</div>\n" +
			"    <section class=\"wrapper\">\n" +
			"\t<div class=\"container\">    \t\n" +
			"        <div class=\"right-bar\">\n" +
			"        \t<header class=\"header changeDiv\">\n" +
			"            \t<div class=\"logo\">\n" +
			"                \t<a href=\"\"><img src=\"../images/logo_img.png\" alt=\"\"></a>\n" +
			"                </div>\n" +
			"                <div id=\"divHeaderright\" class=\"header-right\"><p><strong>Username </strong>- it1802</p><a href=\"/student/password-change.aspx\">Change Password</a><a href=\"../student/singout.aspx\">Logout</a></div>\n" +
			"                <div class=\"clear\"></div>\n" +
			"            </header>            \n" +
			"            <div id=\"divWelcome\"><h1>Welcome : PRATIM</h1></div>\n" +
			"            \n" +
			"            \n" +
			"    <p class=\"inner_head\">\n" +
			"        Student Attendance Report <span style=\"float: right;\">\n" +
			"            <input type=\"submit\" name=\"ctl00$ContentPlaceHolder1$btndashboard\" value=\"Dashboard\" id=\"ContentPlaceHolder1_btndashboard\" /></span>\n" +
			"    </p>\n" +
			"    <div class=\"inner_txt_div\">\n" +
			"        <div>\n" +
			"\t<table class=\"library_table\" cellspacing=\"0\" rules=\"all\" border=\"1\" id=\"ContentPlaceHolder1_gdvstdattendancereport\" style=\"border-collapse:collapse;\">\n" +
			"\t\t<tr>\n" +
			"\t\t\t<th scope=\"col\">SubjectCode</th><th scope=\"col\">PCC-CS302</th><th scope=\"col\">Total</th>\n" +
			"\t\t</tr><tr>\n" +
			"\t\t\t<td>Total Hours</td><td>3</td><td>3</td>\n" +
			"\t\t</tr><tr>\n" +
			"\t\t\t<td>Hours Attended</td><td>2</td><td>2</td>\n" +
			"\t\t</tr><tr>\n" +
			"\t\t\t<td>Percentage</td><td>66.67</td><td>66.67</td>\n" +
			"\t\t</tr>\n" +
			"\t</table>\n" +
			"</div>\n" +
			"    </div>\n" +
			"    <input type=\"hidden\" name=\"ctl00$ContentPlaceHolder1$hdnStudentId\" id=\"ContentPlaceHolder1_hdnStudentId\" value=\"3331\" />\n" +
			"\n" +
			"            <footer class=\"footer\">Copyright © 2017 College of Engineering & Management Kolaghat.</footer>\n" +
			"        </div>        \n" +
			"    </div>\n" +
			"    </section>\n" +
			"    </form>\n" +
			"</body>\n" +
			"</html>\n";

	@Override
	public void onCreate() {
		JobManager.create(this).addJobCreator(new DailyCheckerCreator());
		Logger.getLogger(DTM.getInstance(getBaseContext()).getPWD());
		super.onCreate();
		
		processAttendanceResult();
	}
	
	private void processAttendanceResult() {
		Document doc = null;
		try {
			doc = Jsoup.parse(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Element table; //select the first table.
		Elements rows;
		if (doc != null) {
			table = doc.select("table").get(0);
			rows = table.select("tr");
		} else {
			return;
		}
		
		for (String heads : rows.get(0).text().split("SubjectCode")[1].trim().split(" ")) {
			Logger.getLogger("header ? "+heads);
		}
		
		String header;
		
		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");
			
			header = cols.get(0).text().trim();
			
			for (int z = 1; z < cols.size(); z++) {
				if (header.equals("Total Hours")) {
					Logger.getLogger(header + ":" + cols.get(z).text());
				} else if (header.equals("Hours Attended")) {
					Logger.getLogger(header + ":" + cols.get(z).text());
				} else if (header.equals("Percentage")) {
					Logger.getLogger(header + ":" + cols.get(z).text());
				}
			}
		}
	}
}
