package com.example.wastedfoodteam.utils;

import android.content.Context;
import android.location.Location;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wastedfoodteam.model.Seller;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import  org.mockito.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CommonFunctionTest {
    @Mock
    private Context mContext;
    @Mock
    private ImageView mImageView;
    @Mock
    private EditText mEditText;

    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCurrencyCorrect() {
        String result = CommonFunction.getCurrency((double) 0);
        Assert.assertEquals("0 VND", result);
    }

    @Test
    public void testGetCurrencyCorrect2() {
        String result = CommonFunction.getCurrency((double) 1000000);
        Assert.assertEquals("1,000,000 VND", result);
    }

    @Test
    public void testGetOpenClose() {
        String result = CommonFunction.getOpenClose(new GregorianCalendar(2020, Calendar.DECEMBER, 5, 15, 43).getTime(), new GregorianCalendar(2020, Calendar.DECEMBER, 5, 15, 43).getTime());
        Assert.assertEquals("15:43 - 15:43", result);
    }

    @Test
    public void testGetOpenCloseInputNull() {
        String result = CommonFunction.getOpenClose(null, null);
        Assert.assertEquals("00:00 - 23:59", result);
    }

    @Test
    public void testGetDiscount() {
        String result = CommonFunction.getDiscount(10000d, 20000d);
        Assert.assertEquals("%50", result);
    }

    @Test
    public void testGetDiscount2() {
        String result = CommonFunction.getDiscount(10000d, 100000d);
        Assert.assertEquals("%90", result);
    }

    @Test
    public void testGetDiscount3() {
        String result = CommonFunction.getDiscount(10000d, 0d);
        Assert.assertEquals("%0", result);
    }

    @Test
    public void testGetQuantity() {
        String result = CommonFunction.getQuantity(10, 100);
        Assert.assertEquals("Còn: 10/100", result);
    }

    @Test
    public void testGetQuantity2() {
        String result = CommonFunction.getQuantity(0, 0);
        Assert.assertEquals("Hết hàng", result);
    }

    @Test
    public void testSetQuantityTextView() {
        CommonFunction.setQuantityTextView(null, 0, 0);
    }

    @Test
    public void testGetCurrentDate() {
        String result = CommonFunction.getCurrentDate();
        Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()), result);
    }

    @Test
    public void testCheckEmptyEditText() {
        boolean result = CommonFunction.checkEmptyEditText(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmptyEditText2() {
        EditText editText = Mockito.mock(EditText.class);
        editText.setText("some text");
        boolean result = CommonFunction.checkEmptyEditText(editText);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmptyEditText3() {
        EditText editText = Mockito.mock(EditText.class);
        editText.setText("");
        boolean result = CommonFunction.checkEmptyEditText(editText);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetStringDistance() {
        Seller seller = new Seller();
        Location gps = new Location("");
        String result = CommonFunction.getStringDistance(seller, gps);
        Assert.assertEquals("Không rõ", result);
    }

    @Test
    public void testGetStringDistance2() {
        Seller seller = Mockito.mock(Seller.class);
        seller.setLatitude(1d);
        seller.setLongitude(2d);
        Location gps = Mockito.mock(Location.class);
        gps.setLongitude(2d);
        gps.setLatitude(1d);
        String result = CommonFunction.getStringDistance(seller, gps);
        Assert.assertEquals("0km", result);
    }

    @Test
    public void testGetCurrentDateTime() {
        String result = CommonFunction.getCurrentDatetime();
        Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme