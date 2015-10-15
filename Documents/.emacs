(setq grep-command "grep -nH -r --exclude-dir='svn' --include='*.h' --include='*.cpp' --include='*.c' -e SrchStr *")
(defun select-next-window ()
  "Switch to the next window" 
  (interactive)
  (select-window (next-window)))

(defun select-previous-window ()
  "Switch to the previous window" 
  (interactive)
  (select-window (previous-window)))

(global-set-key (kbd "S-<right>") 'select-next-window)
(global-set-key (kbd "S-<left>")  'select-previous-window)
(global-set-key (kbd "C-~") 'list-buffers);http://xahlee.org/emacs/keyboard_shortcuts.html
(global-set-key (kbd "C-~") 'list-buffers)
(global-set-key (kbd "<f9>") 'gud-step)
(global-set-key (kbd "<f10>") 'gud-next)
(global-set-key (kbd "<f11>") 'gud-finish)
(global-set-key (kbd "<f12>") 'gud-cont)
(global-set-key [f5] 'gud-step)
(global-set-key [f6] 'gud-next)
(global-set-key [f7] 'gud-cont)
(global-set-key [f8] 'gud-finish)
(global-set-key (kbd "s-d") 'kill-ring-save)
(global-set-key (kbd "s-f") 'yank)


(setq tags-table-list
           '("/home/alex/"))
(global-set-key [C-tab] '(lambda () (interactive) (switch-to-buffer (other-buffer))))
;git setup
(add-to-list 'load-path "/home/data/3rd_party/git-emacs")
(require 'git-emacs)
(global-set-key "\M-s\M-s" 'git-status)
(load-file "/usr/lib/gabrielelanaro-emacs-for-python-2f284d1/epy-init.el")
(setq jde-help-remote-file-exists-function '("beanshell"))
(add-to-list 'load-path "~/.emacs.d/jdee-2.4.1/lisp")
(load "~/.emacs.d/jdee-2.4.1/lisp/jde.el")

;bookmark setup
(load "~/.emacs.d/jdee-2.4.1/lisp/bm-1.53.el")
(require 'bm)

(setq bm-highlight-style 'bm-highlight-only-line)

(global-set-key (kbd "<f2>") 'bm-toggle)
(global-set-key (kbd "<C-f2>") 'bm-next)

