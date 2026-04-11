package com.virtualstore.virtualstore.fragment;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.virtualstore.virtualstore.R;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ARFragment extends Fragment implements GLSurfaceView.Renderer {

    private static final String TAG = "ARFragment";
    private GLSurfaceView surfaceView;
    private Session arSession;
    private boolean sessionPaused = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        surfaceView = view.findViewById(R.id.surfaceView);
        ImageButton btnBack = view.findViewById(R.id.btnBackAR);

        // Bouton retour
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // Vérifier si ARCore est supporté
        ArCoreApk.Availability availability = ArCoreApk.getInstance()
                .checkAvailability(requireContext());

        if (availability.isSupported()) {
            setupAR();
        } else {
            Toast.makeText(requireContext(),
                    "ARCore non supporté sur cet appareil",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setupAR() {
        try {
            ArCoreApk.Availability availability = ArCoreApk.getInstance()
                    .checkAvailability(requireContext());

            if (!availability.isSupported()) {
                Toast.makeText(requireContext(),
                        "ARCore non supporté sur cet appareil",
                        Toast.LENGTH_LONG).show();
                return;
            }

            arSession = new Session(requireActivity());
            Config config = new Config(arSession);
            config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
            arSession.configure(config);

            surfaceView.setPreserveEGLContextOnPause(true);
            surfaceView.setEGLContextClientVersion(2);
            surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            surfaceView.setRenderer(this);
            surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "ARCore non disponible : " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, "Erreur AR: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arSession != null) {
            try {
                arSession.resume();
                surfaceView.onResume();
            } catch (Exception e) {
                Log.e(TAG, "Erreur resume: " + e.getMessage());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (arSession != null) {
            surfaceView.onPause();
            arSession.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (arSession != null) {
            arSession.close();
            arSession = null;
        }
    }

    // GLSurfaceView.Renderer
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        if (arSession != null) {
            arSession.setDisplayGeometry(
                    requireActivity().getWindowManager().getDefaultDisplay().getRotation(),
                    width, height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (arSession == null) return;
        try {
            arSession.setCameraTextureName(0);
            Frame frame = arSession.update();
            Camera camera = frame.getCamera();
            if (camera.getTrackingState() == TrackingState.TRACKING) {
                Log.d(TAG, "AR Tracking OK");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur draw: " + e.getMessage());
        }
    }
}