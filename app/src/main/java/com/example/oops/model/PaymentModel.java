package com.example.oops.model;

public class PaymentModel {
    public String orderAmount;
    public String userId;
    public String orderId;
    public String customerName;
    public String customerPhone;
    public String customerEmail;
    public String plan_details_id;

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPlan_details_id() {
        return plan_details_id;
    }

    public void setPlan_details_id(String plan_details_id) {
        this.plan_details_id = plan_details_id;
    }
}
