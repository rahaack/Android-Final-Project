package com.me.mygdxgame;

public interface ActionResolver {
	public void showToast(CharSequence toastMessage, int toastDuration);

	public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText);

	public void openUri(String uri);

	public void showMyList();

	void showShortToast(CharSequence toastMessage);

	void showLongToast(CharSequence toastMessage);
}
