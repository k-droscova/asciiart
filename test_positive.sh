#!/bin/bash

# Paths
IMAGE_PATH="resources/saturn.jpeg"
OUTPUT_DIR="output/saturn_positive_tests"

# Ensure the output directory exists and is empty
if [[ -d "$OUTPUT_DIR" ]]; then
  rm -rf "$OUTPUT_DIR"/*  # Delete all contents inside the directory
fi
mkdir -p "$OUTPUT_DIR"  # Create the directory if it doesn't exist

# Test cases with descriptions and commands
declare -A TEST_CASES=(
  ["simple"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/saturn_table_simple.txt"
  ["table_default"]="--image $IMAGE_PATH --table=default --output-file $OUTPUT_DIR/saturn_table_default.txt"
  ["table_bourke"]="--image $IMAGE_PATH --table=bourke --output-file $OUTPUT_DIR/saturn_table_bourke.txt"
  ["table_bordered"]="--image $IMAGE_PATH --table=bordered \" .:-=+*#%@\" \"[10,20,30,40,50,60,70,80,240]\" --output-file $OUTPUT_DIR/saturn_table_bordered.txt"
  ["custom_table"]="--image $IMAGE_PATH --table=custom \".:-=+*#%@\" --output-file $OUTPUT_DIR/saturn_custom_table.txt"
  ["brightness_positive"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/saturn_brightness_positive.txt --brightness 80"
  ["brightness_negative"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/saturn_brightness_negative.txt --brightness -80"
  ["rotate_90"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/saturn_rotate_90.txt --rotate 90"
  ["rotate_180"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/saturn_rotate_180.txt --rotate 180"
  ["rotate_270"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/saturn_rotate_270.txt --rotate 270"
  ["rotate_minus_90"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/saturn_rotate_minus_90.txt --rotate -90"
  ["invert"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/saturn_invert.txt --invert"
)

for test_name in "${!TEST_CASES[@]}"; do
  echo "Running test case: $test_name"

  # Construct the sbt command in batch mode
  CMD="run ${TEST_CASES[$test_name]}"
  echo "Executing: sbt -batch \"$CMD\""

  # Execute the sbt command in batch mode, capturing both stdout and stderr
  sbt_output=$(sbt -batch "$CMD" 2>&1)

  # Check if the sbt command failed
  if [[ $? -ne 0 ]]; then
    # Print the error message in red
    echo -e "\e[31mTest case $test_name: FAILURE - sbt command failed.\e[0m"
    echo -e "\e[31mError Output:\n$sbt_output\e[0m"
    continue
  fi

  # Check if the output file was created
  OUTPUT_FILE="${TEST_CASES[$test_name]#*--output-file }"
  OUTPUT_FILE="${OUTPUT_FILE%% *}"
  if [[ -f "$OUTPUT_FILE" ]]; then
    echo -e "\e[32mTest case $test_name: SUCCESS - Output generated at $OUTPUT_FILE\e[0m"
  else
    echo -e "\e[31mTest case $test_name: FAILURE - Output file was not created.\e[0m"

    # Highlight if project error messages exist
    if grep -q "Error:" <<< "$sbt_output"; then
      echo -e "\e[31mProject Error Detected:\e[0m"
      echo -e "\e[31m$(grep 'Error:' <<< "$sbt_output")\e[0m"
    fi
  fi
done
