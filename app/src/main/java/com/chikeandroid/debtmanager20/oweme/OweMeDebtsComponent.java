package com.chikeandroid.debtmanager20.oweme;

import com.chikeandroid.debtmanager20.ApplicationComponent;
import com.chikeandroid.debtmanager20.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Chike on 4/14/2017.
 * This is a Dagger component.
 * Because this component depends on the {@link ApplicationComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = OweMeDebtsPresenterModule.class)
public interface OweMeDebtsComponent {

    void inject(OweMeDebtsFragment fragment);
}
