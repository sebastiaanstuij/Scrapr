package com.sebastiaanstuij.scrapr;

public class RowItem {
	 private String website_name;
	 private int pic_id;
	 private String product_name;
	 private String date_added;

	 public RowItem(String member_name, int profile_pic_id, String status,
	   String contactType) {

	  this.website_name = member_name;
	  this.pic_id = profile_pic_id;
	  this.product_name = status;
	  this.date_added = contactType;
	 }

	 public String getMember_name() {
	  return website_name;
	 }

	 public void setMember_name(String member_name) {
	  this.website_name = member_name;
	 }

	 public int getProfile_pic_id() {
	  return pic_id;
	 }

	 public void setProfile_pic_id(int profile_pic_id) {
	  this.pic_id = profile_pic_id;
	 }

	 public String getStatus() {
	  return product_name;
	 }

	 public void setStatus(String status) {
	  this.product_name = status;
	 }

	 public String getContactType() {
	  return date_added;
	 }

	 public void setContactType(String contactType) {
	  this.date_added = contactType;
	 }
}
