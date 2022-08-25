package com.example.mymoneymanager;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class SendMsgDialog extends Dialog {

    public Activity activity;
    public Button yes, no;
    private String generetedMsg;
    private TextView messageContent;
    private EditText email;
    private EditText subjectMsg;

    public SendMsgDialog(Activity activity, String generetedMsg) {
        super(activity);
        this.activity = activity;
        this.generetedMsg = generetedMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_msg_dialog);
        getComponents();
        setListeners();
    }

    private void getComponents() {
        yes = (Button) findViewById(R.id.btn_yesSend);
        no = (Button) findViewById(R.id.btn_noSend);
        messageContent = findViewById(R.id.smsInfo);
        email = findViewById(R.id.emailAddress);
        messageContent.setText(generetedMsg);
        subjectMsg = findViewById(R.id.msgSubject);
    }


    private void setListeners() {
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
                dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        if (pattern.matcher(email).matches()) {
            return true;
        }

        return false;

    }

    private void sendMsg() {
        String emailTo = email.getText().toString();
        String smsContent = messageContent.getText().toString();
        String subject = subjectMsg.getText().toString();
        if ((subject == "") || (subject.isEmpty())) {
            subject = "Wiadomość wygenerowana przez My money Manager";
        }


        if (isEmailValid(emailTo)) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, smsContent);
            email.setType("message/rfc822");
            activity.startActivity(Intent.createChooser(email, "Wybierz e-mail :"));
        } else {
            email.setError("Wprowadź poprawny e-mail");
        }

    }
}