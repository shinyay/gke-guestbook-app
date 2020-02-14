#!/usr/bin/env fish

function open_local
  argparse -n open_local 'h/help' 'u/url=' -- $argv
  or return 1

  if set -lq _flag_help
    echo "open-local.fish -u/--url <MESSAGE>"
    return
  end

  set -lq _flag_message
  or set -l _flag_message "swagger-ui.html"

  open http://localhost:8080/$_flag_message
end

open_local $argv
