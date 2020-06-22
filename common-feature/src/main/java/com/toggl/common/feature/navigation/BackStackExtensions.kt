package com.toggl.common.feature.navigation

import com.toggl.common.feature.R
import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.EditableTimeEntry

typealias BackStack = List<Route>

fun BackStack.push(route: Route) =
    this + route

fun BackStack.pop() =
    this.dropLast(1)

fun <P> BackStack.getRouteParam() =
    filterIsInstance<ParamHolder<P>>().firstOrNull()?.param

fun <P, R> BackStack.letRouteParamIfAny(block: (P) -> R): R? =
    getRouteParam<P>()?.let(block)

inline fun <reified R : Route> BackStack.setRouteParam(paramSetter: () -> R): BackStack =
    map {
        if (it !is R) it
        else paramSetter()
    }