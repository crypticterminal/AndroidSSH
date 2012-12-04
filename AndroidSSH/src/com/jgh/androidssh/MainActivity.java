package com.jgh.androidssh;

import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.jgh.androidssh.channels.CommandExec;
import com.jgh.androidssh.channels.SessionUserInfo;
import com.jgh.androidssh.channels.SshExecutor;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
    
    TextView mTextView;
    EditText mUserEdit;
    EditText mHostEdit;
    EditText mPasswordEdit;
    EditText mCommandEdit;
    Button mButton, mRunButton;
    SessionUserInfo mSUI;
    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        //Set no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_main);
        
        mUserEdit = (EditText)findViewById(R.id.username);
        mHostEdit = (EditText)findViewById(R.id.hostname);
        mPasswordEdit = (EditText)findViewById(R.id.password);
        mButton = (Button)findViewById(R.id.enterbutton);
        mRunButton=(Button)findViewById(R.id.runbutton);
        mTextView = (TextView)findViewById(R.id.sshtext);
        mCommandEdit = (EditText)findViewById(R.id.command);
        
        //set onclicklistener
        mButton.setOnClickListener(this);
        mRunButton.setOnClickListener(this);
        
        
        
    }
    
    public class SshTask extends AsyncTask<Void, Void, Boolean> {
        
        SshExecutor mEx;
        
        public SshTask(SshExecutor exec) {
            mEx = exec;
        }
        
        @Override
        protected Boolean doInBackground(Void... arg0) {
            
            try {
                mEx.executeCommand();
            } catch (JSchException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            return true;
        }
        
        @Override
        protected void onPostExecute(Boolean b) {
            mTextView.setText(mEx.getString());
            
        }
    }
    
    /**
     * Checks if the EditText is empty.
     * @param editText
     * @return true if empty
     */
    private boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString() == null || editText.getText().toString() == "") { return true; }
        return false;
    }
    
    public void onClick(View v) {
        if (v == mButton) {
            if (isEditTextEmpty(mUserEdit) || isEditTextEmpty(mHostEdit) || isEditTextEmpty(mPasswordEdit)) { return; }
            mSUI = new SessionUserInfo(mUserEdit.getText().toString(), mHostEdit.getText().toString(),
                            mPasswordEdit.getText().toString());
            
        }
        
        else if(v==mRunButton){
            //check valid data
            if(isEditTextEmpty(mCommandEdit)){ return;}
            
            if(mSUI==null){ return;}
            
            //run command
            else{
                CommandExec com = new CommandExec(mCommandEdit.getText().toString().trim(), mSUI);
                
                new SshTask(com).execute();
            }
            
        }
        
    }
    
}
