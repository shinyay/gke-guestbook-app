#!/usr/bin/env fish

function create_cluster
  argparse -n create-cluster 'h/help' 'c/cluster=' -- $argv
  or return 1

  if set -lq _flag_help
    echo "create-cluster.fish -c/--cluster <CLUSTER_NAME>"
    return
  end

  if not set -lq _flag_cluster
    echo "create-cluster.fish -c/--cluster <CLUSTER_NAME>" & return
  end

  gcloud container clusters create $_flag_cluster \
    --num-nodes 2 \
    --machine-type n1-standard-1 \
    --zone us-central1-c \
    --scopes cloud-platform
  gcloud container clusters list
end

create_cluster $argv
