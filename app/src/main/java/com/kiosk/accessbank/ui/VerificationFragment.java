package com.kiosk.accessbank.ui;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ExperimentalGetImage;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.camera.CameraManager;
import com.kiosk.accessbank.camera.PositionFaceListener;
import com.kiosk.accessbank.databinding.FragmentVerificationBinding;
import com.kiosk.accessbank.fingerprint.FingerprintHandler;
import com.kiosk.accessbank.util.ToastUtils;
import com.kiosk.accessbank.viewmodel.MainViewModel;
import com.kiosk.accessbank.viewmodel.VerificationViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@ExperimentalGetImage @AndroidEntryPoint
public class VerificationFragment extends Fragment implements FingerprintHandler.FingerprintListener, PositionFaceListener {

    private static final String FINGERPRINT_TAG = "fingerprint";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    FingerprintHandler fingerprintHandler;
    private FragmentVerificationBinding binding;
    private MainViewModel viewModel;
    private VerificationViewModel verificationViewModel;

    private boolean faceInProcess = false;


    private CameraManager cameraManager;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        fingerprintHandler.OnBnBegin();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVerificationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        verificationViewModel = new ViewModelProvider(this).get(VerificationViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fingerprintHandler == null)
            fingerprintHandler = new FingerprintHandler(requireActivity(), this);
        createCameraManager();
        initSelection();
        initObserver();


        binding.cardFingerprint.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "This test using hardcoded dummy data", Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToSelectAccountFragment(getArguments().getString("account_no")));

        });

        showFingerPrint();
        showFaceRecognition();

    }

    @Override
    public void onStart() {
        super.onStart();
        showFingerPrint();
        showFaceRecognition();
    }

    @Override
    public void onResume() {
        super.onResume();
        showFingerPrint();
        showFaceRecognition();
    }

    @Override
    public void onStop() {
        super.onStop();
        fingerprintHandler.OnBnStop();
        cameraManager = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fingerprintHandler.OnBnStop();
        cameraManager = null;
    }

    private void initObserver() {

    }

    private void showFingerPrint() {
        if (binding.layoutFingerprint.getVisibility() == View.VISIBLE) {
            fingerprintHandler.OnBnBegin();
        } else
            fingerprintHandler.OnBnStop();
    }

    private void showFaceRecognition() {
        if (binding.layoutFacial.getVisibility() == View.VISIBLE) {
            cameraManager.startCamera();
        }
    }



    private void createCameraManager() {
        cameraManager = new CameraManager(
                requireContext(),
                binding.preview,
                this,
                binding.graphicOverlayFinder,this
        );
    }

    private void initSelection() {
        binding.buttonFacial.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(binding.getRoot());

            binding.layoutFacial.setVisibility(View.VISIBLE);
            binding.layoutFingerprint.setVisibility(View.GONE);
            showFingerPrint();
            showFaceRecognition();
        });

        binding.buttonFingerprint.setOnClickListener(v -> {
                    TransitionManager.beginDelayedTransition(binding.getRoot());
                    binding.layoutFacial.setVisibility(View.GONE);
                    binding.layoutFingerprint.setVisibility(View.VISIBLE);
                    showFingerPrint();
                    showFaceRecognition();
                }

        );

    }

    @Override
    public void onSuccess(byte[] value, String result) {
//        ToastUtils.show(requireContext(),result);
        Toast.makeText(requireContext(), "This test using hardcoded dummy data", Toast.LENGTH_LONG).show();
        NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToSelectAccountFragment(getArguments().getString("account_no")));
    }

    @Override
    public void onFailed(String message) {
        ToastUtils.show(requireContext(), message);

    }

    @Override
    public void onProgress(float progress) {
        if (binding.layoutFacial.getVisibility() == View.VISIBLE){
            binding.icFacialAlpha.setAlpha(progress > 0.7f ? 0.0f  : progress >0.5f ? 0.4f : progress > 0.2f ? 0.7f  : 1f);
        }
    }

    @Override
    public void onHalfOfProgressReached() {

    }

    @Override
    public void onFUllProgressReached() {
        if (binding.layoutFacial.getVisibility() == View.GONE){
            return;
        }
        if (!faceInProcess){
            faceInProcess = true;
            Toast.makeText(requireContext(), "This test using hardcoded dummy data", Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToSelectAccountFragment(getArguments().getString("account_no")));
            cameraManager = null;
        }

    }

    @Override
    public void onProgressStillNotHalfReached() {

    }
}


