package org.chartsy.main.utils.autocomplete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.chartsy.main.SymbolChanger;

/**
 * 自动完成控件
 * @author Viorel
 */
public abstract class AutoCompleter {

    protected static final Logger LOG = Logger.getLogger(AutoCompleter.class.getPackage().getName());

    // 延迟计时器，让弹出菜单延迟显示
    protected Timer delayTimer;
    // 计时器是否已停止
    protected boolean timerStoped = false;
    // 下拉框
    protected JList list = new JList();
    // 弹出菜单
    protected JPopupMenu popupMenu = new JPopupMenu();
    /**
     * 程序中使用文本框，以便给用户输入数据，在Swing中也提供了同样的文本框组件。在Swing中文本框分为以下几类。
     *  •单行文本框：JTextField
     *  •密码文本框：JPasswordField
     *  •多行文本框：JTextArea
     */
    protected JTextComponent component;

    private static final String AUTOCOMPLETER = "AUTOCOMPLETER";

    public AutoCompleter(JTextComponent comp) {
        delayTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerStoped = false;
                try {
                    showPopup();
                } catch (IOException ex) {
                    return;
                }
            }
        });
        delayTimer.setRepeats(false);

        // 将AUTOCOMPLETER注册为客户属性，并与this实例关联；后面会通过AUTOCOMPLETER获得该客户属性，从而取得this实例
        component = comp;
        component.putClientProperty(AUTOCOMPLETER, this);
        JScrollPane pane = new JScrollPane(list);
        pane.setBorder(null);

        list.setFocusable(true);
        pane.getVerticalScrollBar().setFocusable(false);
        pane.getHorizontalScrollBar().setFocusable(false);
        popupMenu.add(pane);

        // 文本框为JTextField，将VK_DOWN注册为showAction；否则将CTRL+VK_SPACE注册为showAction
        if (component instanceof JTextField) {
            component.registerKeyboardAction(
                    showAction,
                    KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                    JComponent.WHEN_FOCUSED);
            component.getDocument().addDocumentListener(documentListener);
        } else {
            component.registerKeyboardAction(
                    showAction,
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_MASK),
                    JComponent.WHEN_FOCUSED);
        }

        // 将VK_UP注册为upAction
        component.registerKeyboardAction(
                upAction,
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                JComponent.WHEN_FOCUSED);
        // 将VK_ESCAPE注册为hidePopupAction
        component.registerKeyboardAction(
                hidePopupAction,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_FOCUSED);

        // 弹出菜单监听器
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            // 在弹出菜单变为不可见之前调用此方法请注意，JPopupMenu可以随时变为不可见
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // 如果该文本框是SymbolChanger的子控件，则在弹出菜单隐藏前，将VK_ENTER注册为SymbolChanger的submit事件
                // 这里会导致AutoCompleter控件与SymbolChanger耦合
                component.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
                if (component.getParent() instanceof SymbolChanger) {
                    SymbolChanger changer = (SymbolChanger) component.getParent();
                    component.registerKeyboardAction(
                            changer.submit,
                            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                            JComponent.WHEN_FOCUSED);
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        // 列表框不允许获得焦点
        list.setRequestFocusEnabled(false);
    }

    // 停止计时器
    public void stopTimer() {
        delayTimer.stop();
        timerStoped = true;
    }

    // 接受动作
    static Action acceptAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent comp = (JComponent) e.getSource();
            AutoCompleter completer
                    = (AutoCompleter) comp.getClientProperty(AUTOCOMPLETER);
            completer.popupMenu.setVisible(false);
            completer.stopTimer();
            completer.acceptedListItem(completer.list.getSelectedValue());
        }
    };

    DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (!timerStoped) {
                if (delayTimer.isRunning()) {
                    timerStoped = false;
                    delayTimer.restart();
                } else {
                    timerStoped = false;
                    delayTimer.start();
                }
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (!timerStoped) {
                if (delayTimer.isRunning()) {
                    timerStoped = false;
                    delayTimer.restart();
                } else {
                    timerStoped = false;
                    delayTimer.start();
                }
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    };

    // 显示弹出菜单
    private void showPopup() throws IOException {
        // 隐藏弹出菜单
        popupMenu.setVisible(false);
        // 文本框可用、成功更新列表模型、列表模型不为空
        if (component.isEnabled() && updateListData() && list.getModel().getSize() != 0) {
            // 如果文本框不是JTextField，则对文本框添加文档监听器
            if (!(component instanceof JTextField)) {
                component.getDocument().addDocumentListener(documentListener);
            }
            // 为文本框注册回车键事件
            component.registerKeyboardAction(
                    acceptAction,
                    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                    JComponent.WHEN_FOCUSED);
            // 列表模型最多显示10条数据
            int size = list.getModel().getSize();
            list.setVisibleRowCount(size < 10 ? size : 10);

            // 计算弹出菜单位置并显示
            int x = 0;
            try {
                int pos = Math.min(
                        component.getCaret().getDot(),
                        component.getCaret().getMark());
                x = component.getUI().modelToView(component, pos).x;
            } catch (BadLocationException e) {
                LOG.log(Level.SEVERE, null, e);
            }
            popupMenu.show(component, x, component.getHeight());
        } else {
            // 文本框不可用、或者未成功更新列表模型、或者列表模型不为空，则隐藏弹出菜单
            popupMenu.setVisible(false);
        }
        
        // 文本框请求焦点
        component.requestFocus();
    }

    static Action showAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
            if (tf.isEnabled()) {
                // 若弹出菜单已显示，则选择下一个值；否则显示弹出菜单
                if (completer.popupMenu.isVisible()) {
                    completer.selectNextPossibleValue();
                } else {
                    try {
                        completer.showPopup();
                    } catch (IOException ex) {
                        return;
                    }
                }
            }
        }
    };

    static Action upAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
            if (tf.isEnabled()) {
                if (completer.popupMenu.isVisible()) {
                    completer.selectPreviousPossibleValue();
                }
            }
        }
    };

    // 隐藏弹出菜单
    static Action hidePopupAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
            if (tf.isEnabled()) {
                completer.popupMenu.setVisible(false);
            }
        }
    };

    /**
     * Selects the next item in the list. It won't change the selection if the
     * currently selected item is already the last item.
     * 选择列表中的下一个项目。 如果当前所选项目已经是最后一项，则不会更改选择。
     */
    protected void selectNextPossibleValue() {
        int si = list.getSelectedIndex();
        if (si < list.getModel().getSize() - 1) {
            list.setSelectedIndex(si + 1);
            list.ensureIndexIsVisible(si + 1);
        }
    }

    /**
     * Selects the previous item in the list. It won't change the selection if
     * the currently selected item is already the first item.
     * 选择列表中的上一个项目。 如果当前所选项目已经是第一项，则不会更改选择。
     */
    protected void selectPreviousPossibleValue() {
        int si = list.getSelectedIndex();
        if (si > 0) {
            list.setSelectedIndex(si - 1);
            list.ensureIndexIsVisible(si - 1);
        }
    }

    // update list model depending on the data in textfield
    // 根据textfield中的数据更新列表模型
    protected abstract boolean updateListData() throws IOException;

    // user has selected some item in the list. update textfield accordingly...
    // 用户已在列表中选择了一些项目。 相应地更新文本字段...
    protected abstract void acceptedListItem(Object selected);

}
