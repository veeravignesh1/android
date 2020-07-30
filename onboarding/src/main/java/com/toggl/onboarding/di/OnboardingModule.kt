package com.toggl.onboarding.di

import com.toggl.architecture.core.Store
import com.toggl.architecture.core.combine
import com.toggl.architecture.core.pullback
import com.toggl.architecture.core.unwrap
import com.toggl.onboarding.common.domain.OnboardingAction
import com.toggl.onboarding.common.domain.OnboardingReducer
import com.toggl.onboarding.common.domain.OnboardingState
import com.toggl.onboarding.login.domain.LoginAction
import com.toggl.onboarding.login.domain.LoginReducer
import com.toggl.onboarding.login.domain.LoginState
import com.toggl.onboarding.passwordreset.domain.PasswordResetAction
import com.toggl.onboarding.passwordreset.domain.PasswordResetReducer
import com.toggl.onboarding.passwordreset.domain.PasswordResetState
import com.toggl.onboarding.signup.domain.SignUpAction
import com.toggl.onboarding.signup.domain.SignUpReducer
import com.toggl.onboarding.signup.domain.SignUpState
import com.toggl.onboarding.sso.domain.SsoAction
import com.toggl.onboarding.sso.domain.SsoReducer
import com.toggl.onboarding.sso.domain.SsoState
import com.toggl.onboarding.welcome.domain.WelcomeAction
import com.toggl.onboarding.welcome.domain.WelcomeReducer
import com.toggl.onboarding.welcome.domain.WelcomeState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object OnboardingViewModelModule {
    @Provides
    @ExperimentalCoroutinesApi
    internal fun welcomeStore(store: Store<OnboardingState, OnboardingAction>): Store<WelcomeState, WelcomeAction> =
        store.view(
            mapToLocalState = WelcomeState.Companion::fromOnboardingState,
            mapToGlobalAction = OnboardingAction::Welcome
        )

    @Provides
    @ExperimentalCoroutinesApi
    internal fun loginStore(store: Store<OnboardingState, OnboardingAction>): Store<LoginState, LoginAction> =
        store.view(
            mapToLocalState = LoginState.Companion::fromOnboardingState,
            mapToGlobalAction = OnboardingAction::Login
        )

    @Provides
    @ExperimentalCoroutinesApi
    internal fun signUpStore(store: Store<OnboardingState, OnboardingAction>): Store<SignUpState, SignUpAction> =
        store.view(
            mapToLocalState = SignUpState.Companion::fromOnboardingState,
            mapToGlobalAction = OnboardingAction::SignUp
        )

    @Provides
    @ExperimentalCoroutinesApi
    internal fun passwordResetStore(store: Store<OnboardingState, OnboardingAction>): Store<PasswordResetState, PasswordResetAction> =
        store.view(
            mapToLocalState = PasswordResetState.Companion::fromOnboardingState,
            mapToGlobalAction = OnboardingAction::PasswordReset
        )

    @Provides
    @ExperimentalCoroutinesApi
    internal fun ssoStore(store: Store<OnboardingState, OnboardingAction>): Store<SsoState, SsoAction> =
        store.view(
            mapToLocalState = SsoState.Companion::fromOnboardingState,
            mapToGlobalAction = OnboardingAction::Sso
        )
}

@Module
@InstallIn(ApplicationComponent::class)
object OnboardingApplicationModule {
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Provides
    @Singleton
    internal fun onboardingReducer(
        welcomeReducer: WelcomeReducer,
        loginReducer: LoginReducer,
        signUpReducer: SignUpReducer,
        ssoReducer: SsoReducer,
        passwordResetReducer: PasswordResetReducer
    ): OnboardingReducer {
        return combine<OnboardingState, OnboardingAction>(
            welcomeReducer.pullback(
                mapToLocalState = WelcomeState.Companion::fromOnboardingState,
                mapToLocalAction = OnboardingAction::unwrap,
                mapToGlobalState = WelcomeState.Companion::toOnboardingState,
                mapToGlobalAction = OnboardingAction::Welcome
            ),
            loginReducer.pullback(
                mapToLocalState = LoginState.Companion::fromOnboardingState,
                mapToLocalAction = OnboardingAction::unwrap,
                mapToGlobalState = LoginState.Companion::toOnboardingState,
                mapToGlobalAction = OnboardingAction::Login
            ),
            signUpReducer.pullback(
                mapToLocalState = SignUpState.Companion::fromOnboardingState,
                mapToLocalAction = OnboardingAction::unwrap,
                mapToGlobalState = SignUpState.Companion::toOnboardingState,
                mapToGlobalAction = OnboardingAction::SignUp
            ),
            passwordResetReducer.pullback(
                mapToLocalState = PasswordResetState.Companion::fromOnboardingState,
                mapToLocalAction = OnboardingAction::unwrap,
                mapToGlobalState = PasswordResetState.Companion::toOnboardingState,
                mapToGlobalAction = OnboardingAction::PasswordReset
            ),
            ssoReducer.pullback(
                mapToLocalState = SsoState.Companion::fromOnboardingState,
                mapToLocalAction = OnboardingAction::unwrap,
                mapToGlobalState = SsoState.Companion::toOnboardingState,
                mapToGlobalAction = OnboardingAction::Sso
            )
        )
    }
}
