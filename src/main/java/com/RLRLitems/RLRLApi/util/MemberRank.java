package com.RLRLitems.RLRLApi.util;

public enum MemberRank {
	BRONZE(.02),
	SILVER(.04),
	GOLD(.06),
	PLATINUM(.08),
	DIAMOND(.1),
	CHAMPION(.13),
	GRANDCHAMPION(.18);
	
	private double discount;

	MemberRank(double discount) {
		this.setDiscount(discount);
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

}
