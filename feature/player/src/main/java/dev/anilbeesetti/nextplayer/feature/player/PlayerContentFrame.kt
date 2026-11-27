package dev.anilbeesetti.nextplayer.feature.player

import android.graphics.Rect
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import dev.anilbeesetti.nextplayer.feature.player.extensions.toContentScale
import dev.anilbeesetti.nextplayer.feature.player.state.CuesState
import dev.anilbeesetti.nextplayer.feature.player.state.PictureInPictureState
import dev.anilbeesetti.nextplayer.feature.player.state.PlayerEffectsState
import dev.anilbeesetti.nextplayer.feature.player.state.SubtitlePositionState
import dev.anilbeesetti.nextplayer.feature.player.state.VideoZoomAndContentScaleState
import dev.anilbeesetti.nextplayer.feature.player.ui.ShutterView
import dev.anilbeesetti.nextplayer.feature.player.ui.SubtitleConfiguration
import dev.anilbeesetti.nextplayer.feature.player.ui.SubtitleView

private const val NIGHT_MODE_DIM_ALPHA = 0.45f

@OptIn(UnstableApi::class)
@Composable
fun PlayerContentFrame(
    modifier: Modifier = Modifier,
    player: Player,
    pictureInPictureState: PictureInPictureState,
    videoZoomAndContentScaleState: VideoZoomAndContentScaleState,
    effectsState: PlayerEffectsState = PlayerEffectsState(),
    subtitleConfiguration: SubtitleConfiguration,
    cuesState: CuesState,
    subtitlePositionState: SubtitlePositionState,
    videoVisible: Boolean = true,
    displayAspectRatio: Float? = null,
) {
    val presentationState = rememberPresentationState(player = player)
    val sizedVideoModifier = Modifier
        .resizeWithContentScale(
            contentScale = videoZoomAndContentScaleState.videoContentScale
                .toContentScale(),
            sourceSizeDp = presentationState.videoSizeDp?.let { size ->
                size.copy(
                    width = with(LocalDensity.current) {
                        (
                            displayAspectRatio?.let {
                                size.height * it
                            } ?: size.width
                            ).toDp().value
                    },
                    height = with(LocalDensity.current) { size.height.toDp().value },
                )
            },
        )
    // Subtitle bitmaps follow zoom and pan, but are never mirrored or flipped.
    val bitmapModifier = sizedVideoModifier
        .graphicsLayer {
            scaleX = videoZoomAndContentScaleState.zoom
            scaleY = videoZoomAndContentScaleState.zoom
            translationX = videoZoomAndContentScaleState.offset.x
            translationY = videoZoomAndContentScaleState.offset.y
        }
    val videoModifier = sizedVideoModifier
        .graphicsLayer {
            val mirror = if (effectsState.isMirrored) -1f else 1f
            val flip = if (effectsState.isFlipped) -1f else 1f
            scaleX = videoZoomAndContentScaleState.zoom * mirror
            scaleY = videoZoomAndContentScaleState.zoom * flip
            translationX = videoZoomAndContentScaleState.offset.x
            translationY = videoZoomAndContentScaleState.offset.y
        }
    Box(modifier.fillMaxSize()) {
        if (videoVisible) {
            PlayerSurface(
                player = player,
                surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                modifier = videoModifier
                    .onGloballyPositioned {
                        val bounds = it.boundsInWindow()
                        val rect = Rect(
                            bounds.left.toInt(),
                            bounds.top.toInt(),
                            bounds.right.toInt(),
                            bounds.bottom.toInt(),
                        )
                        pictureInPictureState.setVideoViewRect(rect)
                    },
            )
        }

        AnimatedVisibility(
            visible =
                subtitleConfiguration.controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
            )
        }
        if (effectsState.isNightMode) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = NIGHT_MODE_DIM_ALPHA)),
            )
        }
        SubtitleView(
            bitmapModifier = bitmapModifier,
            cuesState = cuesState,
            positionState = subtitlePositionState,
            isInPictureInPictureMode = pictureInPictureState.isInPictureInPictureMode,
            configuration = subtitleConfiguration,
        )

        if (videoVisible && presentationState.coverSurface) {
            ShutterView()
        }
    }
}
