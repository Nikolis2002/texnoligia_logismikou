package com.ceid.ui;

import android.app.Application;

import com.ceid.model.users.User;

public class App extends Application
{
	private User currentUser;

	public User getUser() {
		return currentUser;
	}

	public void setUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
