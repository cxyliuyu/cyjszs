package com.cxyliuyu.cyjszs.value;


public class JiankaotiaoValue {
	public int id = 0;
	public String fuser_name = null;
	public String course = null;
	public String test_time = null;
	public String classroom = null;
	public String fuser_id = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFuser_name() {
		return fuser_name;
	}

	public void setFuser_name(String fuser_name) {
		this.fuser_name = fuser_name;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getTest_time() {
		return test_time;
	}

	public void setTest_time(String test_time) {
		this.test_time = test_time;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public String getFuser_id() {
		return fuser_id;
	}

	public void setFuser_id(String fuser_id) {
		this.fuser_id = fuser_id;
	}

	public JiankaotiaoValue(int id, String fuser_name, String course,
			String test_time, String classroom, String fuser_id) {
		this.id = id;
		this.fuser_name = fuser_name;
		this.course = course;
		this.test_time = test_time;
		this.classroom = classroom;
		this.fuser_id = fuser_id;
	}

	public JiankaotiaoValue() {

	}

	public String getIdByString() {
		String id = String.valueOf(this.id);
		return id;
	}

	public String getTrueTestTime() {
		String trueTestTime = null;
		trueTestTime = this.test_time.substring(0, 4) + "年"
				+ this.test_time.substring(4, 6) + "月"
				+ this.test_time.substring(6, 8) + "日"
				+ this.test_time.substring(8, 10) + "时"
				+ this.test_time.substring(10, 12) + "分";
		//Log.i("substring", trueTestTime);
		return trueTestTime;
	}
}
