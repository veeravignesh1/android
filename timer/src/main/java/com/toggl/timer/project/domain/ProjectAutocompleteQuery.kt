package com.toggl.timer.project.domain

sealed class ProjectAutocompleteQuery {
    abstract val name: String

    object None : ProjectAutocompleteQuery() {
        override val name: String
            get() = ""
    }

    data class WorkspaceQuery(override val name: String) : ProjectAutocompleteQuery()
    data class ClientQuery(override val name: String) : ProjectAutocompleteQuery()
}