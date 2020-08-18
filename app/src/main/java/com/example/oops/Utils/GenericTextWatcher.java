package com.example.oops.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class GenericTextWatcher implements TextWatcher {
    private View view;
    public GenericTextWatcher(View view)
    {
        this.view = view;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // TODO Auto-generated method stub
        String text = editable.toString();
      //  switch(view.getId())
       /* {

            case R.id.editText1:
                if(text.length()==1)
                    et2.requestFocus();
                break;
            case R.id.editText2:
                if(text.length()==1)
                    et3.requestFocus();
                else if(text.length()==0)
                    et1.requestFocus();
                break;
            case R.id.editText3:
                if(text.length()==1)
                    et4.requestFocus();
                else if(text.length()==0)
                    et2.requestFocus();
                break;
            case R.id.editText4:
                if(text.length()==0)
                    et3.requestFocus();
                break;
        }*/
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }
}
