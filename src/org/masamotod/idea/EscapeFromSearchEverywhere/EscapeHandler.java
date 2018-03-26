package org.masamotod.idea.EscapeFromSearchEverywhere;

import com.intellij.ide.actions.SearchEverywhereAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.SearchTextField;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EscapeHandler implements ApplicationComponent {
  private static final Logger LOG = Logger.getInstance(EscapeHandler.class);

  private final MySearchEverywhereFieldKeyListener mySearchEverywhereFieldKeyListener = new MySearchEverywhereFieldKeyListener();

  @Override
  public void initComponent() {
    ActionManager actionManager = ActionManager.getInstance();

    actionManager.addAnActionListener(new AnActionListener() {
      @Override
      public void beforeActionPerformed(AnAction action, DataContext context, AnActionEvent event) {
      }

      @Override
      public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
        if (action instanceof SearchEverywhereAction) {
          installEscapeHandlerToVisiblePopup();
        }
      }
    });
  }

  private void installEscapeHandlerToVisiblePopup() {
    SearchTextField searchTextField = findSearchEverywhereField();

    if (searchTextField != null) {
      searchTextField.addKeyboardListener(mySearchEverywhereFieldKeyListener);
    }
  }

  @Nullable
  private static SearchTextField findSearchEverywhereField() {
    for (Window window : Window.getWindows()) {
      if (window instanceof JWindow
          && Window.Type.POPUP.equals(window.getType())) {
        SearchTextField searchTextField = UIUtil.findComponentOfType(((JWindow)window).getRootPane(), SearchTextField.class);

        if (searchTextField != null
            && searchTextField.getClass().toString().contains("com.intellij.ide.actions.SearchEverywhereAction")) {
          return searchTextField;
        }
      }
    }
    return null;
  }

  @Override
  public void disposeComponent() {
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "EscapeHandler";
  }

  static class MySearchEverywhereFieldKeyListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        // Simulate 'focus lost' event.
        e.getComponent().dispatchEvent(new FocusEvent(e.getComponent(), FocusEvent.FOCUS_LOST));
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
  }
}
