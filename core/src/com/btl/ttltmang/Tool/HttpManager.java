package com.btl.ttltmang.Tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.btl.ttltmang.Main;

import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

public class HttpManager implements Net.HttpResponseListener
{
    private String result;
    private byte[] byteResult;

    Net.HttpRequest request;
    public static int LOI = 0;

    public HttpManager()
    {

        request = new Net.HttpRequest();
        request.setMethod(Net.HttpMethods.GET); //or POST
        request.setContent(""); //you can put here some PUT/GET content
        request.setUrl("https://any-api.com/");
        Gdx.net.sendHttpRequest(request, this);
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse)
    {
        if( httpResponse.getStatus().getStatusCode() != 200 )
        {
            //ERROR
            float errorCode = httpResponse.getStatus().getStatusCode();


        }
        else
        {
            byte[] byteResult = httpResponse.getResult();
            LOI = 0;

        }
    }

    @Override
    public void failed(Throwable t)
    {
        LOI = 1;

    }

    @Override
    public void cancelled()
    {
        // TODO Auto-generated method stub
    }
}