package com.expert.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Expert {

	private final StringProperty thId;
	private final StringProperty thName;
	private final StringProperty thSex;
	private final StringProperty thPhone;
	private final StringProperty thField;
	private final StringProperty thProfessionalTitle;
	
	public Expert() {
		this("", "", "", "", "", "");
	}

	public Expert(String id, String name, String sex, String phone,
			String field,String professionalTitle) {
		this.thId = new SimpleStringProperty(id);
		this.thName = new SimpleStringProperty(name);
		this.thSex = new SimpleStringProperty(sex);
		this.thPhone = new SimpleStringProperty(phone);
		this.thField = new SimpleStringProperty(field);
		this.thProfessionalTitle = new SimpleStringProperty(professionalTitle);
	}
	public Expert(ExpertCopy expertCopy) {
		this(expertCopy.getTh_id(), expertCopy.getTh_name(), expertCopy.getTh_sex(), 
				expertCopy.getTh_phone(), expertCopy.getTh_field(), expertCopy.getTh_professional_title());
	}
	

	public String getThId() {
		return thId.get();
	}
	public void setThId(String id) {
		this.thId.set(id);
	}
	
	public StringProperty thIdProperty() {
		return thId;
	}
	
	public String getThName() {
		return thName.get();
	}
	public void setThName(String name) {
		this.thName.set(name);
	}
	public StringProperty thNameProperty() {
		return thName;
	}
	
	public String getThSex() {
		return thSex.get();
	}
	public void setThSex(String Sex) {
		this.thSex.set(Sex);
	}
	public StringProperty thSexProperty() {
		return thSex;
	}
	
	public String getThPhone() {
		return thPhone.get();
	}
	public void setThPhone(String phone) {
		this.thPhone.set(phone);
	}
	public StringProperty thPhoneProperty() {
		return thPhone;
	}

	public String getThField() {
		return thField.get();
	}
	public void setThField(String field) {
		this.thField.set(field);
	}
	public StringProperty thFieldProperty() {
		return thField;
	}
	
	public String getThProfessionalTitle() {
		return thProfessionalTitle.get();
	}
	public void setThProfessionalTitle(String ProfessionalTitle) {
		this.thProfessionalTitle.set(ProfessionalTitle);
	}
	public StringProperty thProfessionalTitleProperty() {
		return thProfessionalTitle;
	}

	@Override
	public String toString() {
		return "Expert [thId=" + thId + ", thName=" + thName + ", thSex=" + thSex + ", thPhone=" + thPhone + ", thField="
				+ thField + ", thProfessionalTitle=" + thProfessionalTitle + "]";
	}
	
}
