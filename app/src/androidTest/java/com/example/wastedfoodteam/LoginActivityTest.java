package com.example.wastedfoodteam;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.wastedfoodteam.login.FragmentLoginPartner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    LoginActivity loginActivity = null;
    FragmentLoginPartner fragmentLoginPartner = null;
    @Before
    public void setUp() {
        this.loginActivity = new LoginActivity();
        this.fragmentLoginPartner = new FragmentLoginPartner();
    }

    @Test
    public void useAppContext() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        assertEquals("com.example.wastedfoodteam", context.getOpPackageName());
    }

    @Test
    public void otherSample() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        assertEquals("", context.getOpPackageName());
    }

    @After
    public void tearDown() {
    }
}