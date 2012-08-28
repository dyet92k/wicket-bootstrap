package de.agilecoders.wicket.settings;

import com.google.common.base.Strings;
import de.agilecoders.wicket.Bootstrap;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;

/**
 * An {@link ActiveThemeProvider} implementation that stores the active theme
 * in the user session as style.
 *
 * @author miha
 * @version 1.0
 */
public class SessionThemeProvider implements ActiveThemeProvider {

    /**
     * @return the {@link ThemeProvider} implementation
     */
    private ThemeProvider themeProvider() {
        if (Application.exists()) {
            return Bootstrap.getSettings(Application.get()).getThemeProvider();
        } else {
            throw new WicketRuntimeException("no application assigned to current thread");
        }
    }

    @Override
    public Theme getActiveTheme() {
        String style = Session.get().getStyle();

        if (Strings.isNullOrEmpty(style)) {
            return themeProvider().defaultTheme();
        } else {
            return themeProvider().byName(style);
        }
    }

    @Override
    public void setActiveTheme(String themeName) {
        setActiveTheme(themeProvider().byName(themeName));
    }

    @Override
    public void setActiveTheme(Theme theme) {
        assertBoundSession();

        if (theme != null) {
            Session.get().setStyle(theme.name());
        } else {
            Session.get().setStyle(null);
        }
    }

    /**
     * checks on existing session, if there isn't one it will be created.
     */
    private void assertBoundSession() {
        Session session = Session.get();

        if (session.isTemporary()) {
            session.bind();
        }
    }
}
