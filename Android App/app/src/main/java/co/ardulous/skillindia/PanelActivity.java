package co.ardulous.skillindia;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class PanelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static FragmentManager fm;
    private static android.app.FragmentManager fragmentManager;
    private static String currentString;

    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private int tapCount;
    private long timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        ((NavigationView) findViewById(R.id.navView)).setNavigationItemSelectedListener(this);

        tapCount = 0;

        fm = getSupportFragmentManager();
        fragmentManager = getFragmentManager();
        switchFragment(new UpdatesFragment(), getString(R.string.home));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                switchFragment(new UpdatesFragment(), getString(R.string.home));
                break;

            case R.id.about:
                switchFragment(new AboutFragment(), getString(R.string.about));
                break;

            case R.id.contribute:
                break;

            case R.id.gallery:
                break;

            case R.id.locate:
                break;

            case R.id.login:
                if(fragmentManager.findFragmentByTag(getString(R.string.login)) == null) {
                    fragmentManager.beginTransaction().
                            replace(R.id.fragContainer, AccountFragment.newInstance(this), getString(R.string.login)).commit();
                    fm.beginTransaction().remove(fm.findFragmentByTag(currentString)).commit();
                }
                break;

            case R.id.partner:
                switchFragment(new TrainingFragment(), getString(R.string.partner));
                break;

            case R.id.contact:
                break;

            case R.id.feedback:
                break;

            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey!!I installed the Skill India App. It is great.\nInstall it here:\n" +
                                getString(R.string.app_address)
                );
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share using"));
                break;

            default:
                return false;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if(fragmentManager.findFragmentByTag(getString(R.string.login)) != null) {
            switchFragment(new UpdatesFragment(), getString(R.string.home));
            fm.beginTransaction().replace(R.id.fragContainer, new UpdatesFragment(), getString(R.string.home)).commit();
        } else if(tapCount == 0) {
            final Snackbar snackBar = Snackbar
                    .make(findViewById(R.id.coordinatorView), "Tap Again to Exit", Snackbar.LENGTH_LONG);

            snackBar.setAction("EXIT", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            snackBar.show();

            tapCount++;
            timeStamp = System.currentTimeMillis();
        } else if(System.currentTimeMillis()-timeStamp <= 1000) {
            super.onBackPressed();
        } else {
            tapCount = 0;
            onBackPressed();
        }
    }

    private void switchFragment(Fragment thisFragment, String thisString) {
        android.app.Fragment fragment = fragmentManager.findFragmentByTag(getString(R.string.login));
        if(fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }

        if(!thisString.equals(currentString)) {
            fm.beginTransaction().replace(R.id.fragContainer, thisFragment, thisString).commit();
            currentString = thisString;
        }
    }
}
