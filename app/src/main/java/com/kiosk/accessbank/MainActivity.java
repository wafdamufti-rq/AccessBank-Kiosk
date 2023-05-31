package com.kiosk.accessbank;

import static android.view.View.GONE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.databinding.ActivityMainBinding;
import com.kiosk.accessbank.util.KioskManager;
import com.kiosk.accessbank.viewmodel.MainViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 255;
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    @Inject
    public KioskManager kioskManager;
    NavController navController;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNavController();
        initListener();
        initView();
        kioskManager.setUpAdmin(this);
    }

    private void initView() {
        initLabelKioskMode();
    }

    private void initLabelKioskMode() {
        if (kioskManager.isLockedMode())
            binding.buttonKioskMode.setText("Disable kiosk mode");
        else
            binding.buttonKioskMode.setText("Enable kiosk mode");
    }

    private void initListener() {
        binding.buttonBack.setOnClickListener(v -> navController.popBackStack());
        binding.buttonCancel.setOnClickListener(v -> {
            goToLoginAsSingleTop();
        });
        binding.buttonNext.setOnClickListener(v -> viewModel.next(Objects.requireNonNull(navController.getCurrentDestination())));
        binding.buttonSubmit.setOnClickListener(v -> viewModel.submit(Objects.requireNonNull(navController.getCurrentDestination())));

        binding.buttonKioskMode.setOnClickListener(v -> {
            kioskManager.enableKioskMode(!kioskManager.isLockedMode());
            initLabelKioskMode();
        });

        binding.buttonHome.setOnClickListener(v -> goToLoginAsSingleTop());
    }

    private void goToLoginAsSingleTop() {
        viewModel.clearState();
        navController.navigate(R.id.loginFragment, null, new NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setLaunchSingleTop(true).build());
    }

    private void initNavController() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();


        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            validateNavDestination(navDestination);
        });
    }

    private void validateNavDestination(NavDestination navDestination) {
        if (navDestination.getId() == R.id.loginFragment) {
            binding.buttonBack.setVisibility(GONE);
            binding.buttonCancel.setVisibility(GONE);
            binding.buttonNext.setVisibility(View.VISIBLE);
            binding.buttonSubmit.setVisibility(GONE);
            binding.buttonHome.setVisibility(GONE);

        } else if (navDestination.getId() == R.id.verificationFragment) {
            binding.buttonBack.setVisibility(GONE);
            binding.buttonCancel.setVisibility(View.VISIBLE);
            binding.buttonNext.setVisibility(GONE);
            binding.buttonSubmit.setVisibility(GONE);
            binding.buttonHome.setVisibility(GONE);

        } else if (navDestination.getId() == R.id.selectAccountFragment) {
            binding.buttonBack.setVisibility(GONE);
            binding.buttonCancel.setVisibility(View.VISIBLE);
            binding.buttonNext.setVisibility(GONE);
            binding.buttonSubmit.setVisibility(GONE);
            binding.buttonHome.setVisibility(GONE);

        } else if (navDestination.getId() == R.id.selectServiceFragment) {
            binding.buttonBack.setVisibility(View.VISIBLE);
            binding.buttonCancel.setVisibility(View.VISIBLE);
            binding.buttonNext.setVisibility(GONE);
            binding.buttonSubmit.setVisibility(GONE);
            binding.buttonHome.setVisibility(GONE);

        } else if (navDestination.getId() == R.id.selectOptionFragment) {
            binding.buttonBack.setVisibility(View.VISIBLE);
            binding.buttonCancel.setVisibility(View.VISIBLE);
            binding.buttonNext.setVisibility(GONE);
            binding.buttonSubmit.setVisibility(GONE);
            binding.buttonHome.setVisibility(GONE);

        } else if (navDestination.getId() == R.id.updateInformationFragment) {
            binding.buttonBack.setVisibility(View.VISIBLE);
            binding.buttonCancel.setVisibility(GONE);
            binding.buttonNext.setVisibility(GONE);
            binding.buttonSubmit.setVisibility(View.VISIBLE);
            binding.buttonHome.setVisibility(GONE);

        } else if (navDestination.getId() == R.id.confirmationUpdateInfoFragment) {
            binding.buttonBack.setVisibility(GONE);
            binding.buttonCancel.setVisibility(GONE);
            binding.buttonNext.setVisibility(GONE);
            binding.buttonSubmit.setVisibility(GONE);
            binding.buttonHome.setVisibility(View.VISIBLE);
        }
    }

    private Boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        validatePermission(requestCode);
    }

    private void validatePermission(int requestCode) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (allPermissionsGranted()) {

            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
    }


}