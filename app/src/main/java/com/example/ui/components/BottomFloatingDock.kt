package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.DockTab
import com.example.ui.theme.DockBackground
import com.example.ui.theme.DockIconActive
import com.example.ui.theme.DockIconInactive
import com.example.ui.theme.DockItemActiveBg

@Composable
fun BottomFloatingDock(
    currentTab: DockTab,
    onTabSelected: (DockTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(30.dp),
                spotColor = Color.Black.copy(alpha = 0.35f)
            )
            .clip(RoundedCornerShape(30.dp))
            .background(DockBackground)
            .height(54.dp)
            .width(224.dp)
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Library / Grid
            DockIconButton(
                isSelected = currentTab == DockTab.LIBRARY,
                onClick = { onTabSelected(DockTab.LIBRARY) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == DockTab.LIBRARY) Icons.Filled.GridView else Icons.Outlined.GridView,
                        contentDescription = "Library",
                        tint = if (currentTab == DockTab.LIBRARY) DockIconActive else DockIconInactive,
                        modifier = Modifier.size(20.dp)
                    )
                },
                testTag = "dock_tab_library"
            )

            // Tab 2: Collections / Shelves (Active in screenshots)
            DockIconButton(
                isSelected = currentTab == DockTab.COLLECTIONS,
                onClick = { onTabSelected(DockTab.COLLECTIONS) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.CollectionsBookmark,
                        contentDescription = "Collections",
                        tint = if (currentTab == DockTab.COLLECTIONS) DockIconActive else DockIconInactive,
                        modifier = Modifier.size(20.dp)
                    )
                },
                testTag = "dock_tab_collections"
            )

            // Tab 3: Stats & Reading Analytics
            DockIconButton(
                isSelected = currentTab == DockTab.STATS,
                onClick = { onTabSelected(DockTab.STATS) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == DockTab.STATS) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                        contentDescription = "Statistiques",
                        tint = if (currentTab == DockTab.STATS) DockIconActive else DockIconInactive,
                        modifier = Modifier.size(20.dp)
                    )
                },
                testTag = "dock_tab_stats"
            )

            // Tab 4: Settings
            DockIconButton(
                isSelected = currentTab == DockTab.SETTINGS,
                onClick = { onTabSelected(DockTab.SETTINGS) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = if (currentTab == DockTab.SETTINGS) DockIconActive else DockIconInactive,
                        modifier = Modifier.size(20.dp)
                    )
                },
                testTag = "dock_tab_settings"
            )
        }
    }
}

@Composable
private fun DockIconButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    val bgAnimColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) DockItemActiveBg else Color.Transparent,
        animationSpec = androidx.compose.animation.core.spring(stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow),
        label = "dock_bg_color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "dock_scale"
    )

    Box(
        modifier = Modifier
            .size(38.dp)
            .scale(scale)
            .clip(RoundedCornerShape(11.dp))
            .background(bgAnimColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, radius = 20.dp),
                onClick = onClick
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

