package com.example.wastedfoodteam.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastedfoodteam.model.Account;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ReportDialogTest {
    @Mock
    ImageView ivReport;
    @Mock
    TextView tvAccused;
    @Mock
    EditText etContent;
    @Mock
    CameraStorageFunction cameraStorageFunction;
    @Mock
    Context context;
    @Mock
    LayoutInflater inflater;
    @Mock
    Account account;
    @InjectMocks
    ReportDialog reportDialog;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDisplayReportDialog() {
        when(cameraStorageFunction.getImage_uri()).thenReturn(null);
        when(account.getId()).thenReturn(0);

        reportDialog.displayReportDialog();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme