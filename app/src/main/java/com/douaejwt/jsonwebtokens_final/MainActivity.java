package com.douaejwt.jsonwebtokens_final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Key key = MacProvider.generateKey();

        String compactJws = Jwts.builder()
                .setSubject("tpjws")
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        String msgTxt =  Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(compactJws)
                .getBody().getSubject();


        TextView token = (TextView)findViewById(R.id.textView2);
        token.setText(msgTxt);

        ///appelSynchrone();
        appelAsynchrone();
    }

    private void appelSynchrone() {
        new ListReposTask().execute("mahrougd");
    }

    private void appelAsynchrone() {
        GithubAPI githubService = new RestAdapter.Builder()
                .setEndpoint(GithubAPI.ENDPOINT)
                .setLog(new AndroidLog("retrofit"))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(GithubAPI.class);

        githubService.listReposAsync("mahrougd", new Callback<List<Depot>>() {
            @Override
            public void success(List<Depot> depots, Response response) {
                afficherDepot(depots);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void afficherDepot(List<Depot> depots) {
        Toast.makeText(this, "nombre de dépots : " + depots.size(), Toast.LENGTH_SHORT).show();
    }

    public void notAllowed() {
        Toast.makeText(this, "Impossible d'effectuer cette action", Toast.LENGTH_SHORT).show();
    }


    class ListReposTask extends AsyncTask<String, Void, List<Depot>> {

        @Override
        protected List<Depot> doInBackground(String... params) {
            GithubAPI githubService = new RestAdapter.Builder()
                    .setEndpoint(GithubAPI.ENDPOINT)
                    .setErrorHandler(new ErrorHandler() {
                        @Override
                        public Throwable handleError(RetrofitError cause) {
                            Response r = cause.getResponse();
                            if (r != null && r.getStatus() == 405) {
                                MainActivity.this.notAllowed();
                            }
                            return cause;
                        }
                    })
                    .setRequestInterceptor(
                            new RequestInterceptor() {
                                @Override
                                public void intercept(RequestFacade request) {
                                    //ajoute "baerer: 1234567890" en header de chaque requête
                                    request.addHeader("bearer", "1234567890");
                                }
                            }
                    ).build().create(GithubAPI.class);

            String user = params[0];
            List<Depot> repoList = githubService.listRepos(user);

            return repoList;
        }

        @Override
        protected void onPostExecute(List<Depot> repos) {
            super.onPostExecute(repos);
            afficherDepot(repos);
        }
    }
}