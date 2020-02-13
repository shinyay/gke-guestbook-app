#!/usr/bin/env fish

function delete_image
  argparse -n delete-image 'h/help' 'i/image=' -- $argv
  or return 1

  if set -lq _flag_help
    echo "delete-image.fish -i/--image <CONTAINER_IMAGE_NAME>"
    return
  end

  if not set -lq _flag_image
    echo "delete-image.fish -i/--image <CONTAINER_IMAGE_NAME>" & return
  end

  gcloud container images delete --force-delete-tags --quiet $_flag_image
  gcloud container images list
end

delete_image $argv
