package ch.ethz.inf.vs.a2.resource;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Vibrator;

import java.io.IOException;
import java.net.URI;

import ch.ethz.inf.vs.a2.http.HttpResponse;
import ch.ethz.inf.vs.a2.http.ParsedRequest;

/**
 * Created by linus on 22.10.2016.
 */

public class FlashlightResource extends Resource {

    private boolean hasFlashlight;
    private String html;
    private Camera camera;

    public FlashlightResource(URI uri, boolean hasFlashlight) {
        super(uri);
        this.hasFlashlight = hasFlashlight;

        StringBuilder sb = new StringBuilder();
        sb.append("<h3>").append("Flashlight").append("</h3>");
        sb.append("<p>").append("Turn Flashlight on/off.").append("</p>");
        sb.append("<form method='post'>");
        sb.append("<input type='submit' name='state' value='On'>").append("</input>");
        sb.append("<input type='submit' name='state' value='Off'>").append("</input>");
        sb.append("</form>");
        sb.append("<p>").append("<a href='/'>").append("Return to root").append("</a>").append("</p>");
        html = sb.toString();
    }

    @Override
    protected String get(ParsedRequest request) {
        return HttpResponse.generateResponse("200 OK", html);
    }

    @Override
    protected String post(ParsedRequest request) {
        if (!request.header.containsKey("Content-Length") || Integer.parseInt(request.header.get("Content-Length")) == 0)
            return HttpResponse.generateErrorResponse("406 Not Acceptable", "No content in POST request.");

        if (!request.content.containsKey("state"))
            return HttpResponse.generateErrorResponse("404 Not Found", "Device has no flashlight.");

        if (!hasFlashlight)
            return HttpResponse.generateResponse("200 OK", html + "<p>Device has no camera.</p>");

        String stateString = request.content.get("state");
        initCamera();
        Camera.Parameters p = camera.getParameters();
        String info;
        if ("On".equals(stateString)) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            try {
                SurfaceTexture previewTexture = new SurfaceTexture(0);
                camera.setPreviewTexture(previewTexture);
            } catch (IOException ex) {
                // Ignore
            }
            camera.startPreview();
            info = "<p>Device flashlight is now on.</p>";
        } else if (("Off").equals(stateString)) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
            releaseCamera();
            info = "<p>Device flashlight is now off.</p>";
        } else
            return HttpResponse.generateErrorResponse("400 Bad Request", "State value is neither 'On' nor 'Off'.");

        return HttpResponse.generateResponse("200 OK", html + info);
    }

    void initCamera(){
        if(camera == null) {
            int cameraId = 0;
            for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
                Camera.CameraInfo camInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(camNo, camInfo);
                if (camInfo.facing == (Camera.CameraInfo.CAMERA_FACING_BACK))
                    cameraId = camNo;
            }

            camera = Camera.open(cameraId);
        }
    }

    void releaseCamera() {
        if(camera != null){
            camera.release();
        }
        camera = null;
    }
}
