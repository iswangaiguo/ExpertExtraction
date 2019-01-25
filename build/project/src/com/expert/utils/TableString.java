package com.expert.utils;

public class TableString {

	public static String CREATE_EXPERT_TABLE = "CREATE TABLE IF NOT EXISTS 'expert' ("
			+ "'th_id' INT NOT NULL PRIMARY KEY,"
			+ "'th_name' CHAR(10) NOT NULL,"
			+ "'th_sex' CHAR(1) NOT NULL,"
			+ "'th_age' INT NOT NULL,"
			+ "'th_field' CHAR(30) NOT NULL,"
			+ "'th_professional_title' CHAR(10) NOT NULL)";
	
	public static String CREATE_PROJECTS_TABLE = "CREATE TABLE IF NOT EXISTS 'projects'("
			+ "'project_id' INT NOT NULL PRIMARY KEY,"
			+ "'project_name' CHAR(50) NOT NULL,"
			+ "'project_time' CHAR(15) NOT NULL,"
			+ "'project_num' INT NOT NULL,"
			+ "'project_location' CHAR(50) NOT NULL,"
			+ "'Project_fields' CHAR(50) NOT NULL," 
			+ "'Project_expertsId' CHAR(10) NOT NULL)";
	
	
	
	public static String[] FIELD= {
		"冶金工程","资源循环科学与工程","矿物加工工程","材料成型及控制工程","金属材料工程","无机非金属材料工程","材料物理","材料科学与工程","焊接技术与工程","材料化学","化学工程与工艺",
		"应用化学","高分子材料与工程","制药工程","化学生物学","能源化学工程","建筑环境与能源应用工程","给排水科学与工程","土木工程","安全工程","工程管理","建筑学",
		"城乡规划","道路桥梁与渡河工程","机械设计制造及其自动化","过程装备与控制工程","车辆工程","机械工程","自动化","测控技术与仪器","通信工程","电子信息工程","电气工程及其自动化",
		"电气工程与智能控制","计算机科学与技术","软件工程","网络工程","物联网工程","信息与计算科学","数学与应用数学","光电信息科学与工程","工业工程","光源与照明","信息管理与信息系统","物流工程",
		"工程造价","会计学","财务管理","工商管理","市场营销","人力资源管理","旅游管理","审计学","国际经济与贸易","经济统计学","金融学","经济学","经济与金融","国际商务","国际经济与贸易（中外合作办学）","会计学（中外合作办学）","互联网金融",
		"法学","行政管理","公共事业管理","劳动与社会保障","英语","秘书学","翻译","能源与动力工程","环境工程","环保设备工程","新能源科学与工程","工业设计","产品设计","环境设计","视觉传达设计","数字媒体艺术","公共艺术","体育经济与管理"
	};
}
