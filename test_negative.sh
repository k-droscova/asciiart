#!/bin/bash

# Paths
IMAGE_PATH="resources/saturn.jpeg"
IMAGE_PATH_NONEXISTENT="resources/nonexistent.jpeg"
IMAGE_PATH_UNSUPPORTED="resources/bird.webp"
OUTPUT_DIR="output/saturn_negative_tests"

# Ensure the output directory exists and is empty
if [[ -d "$OUTPUT_DIR" ]]; then
  rm -rf "$OUTPUT_DIR"/*  # Delete all contents inside the directory
fi
mkdir -p "$OUTPUT_DIR"  # Create the directory if it doesn't exist

# Negative test cases with invalid inputs
declare -A NEGATIVE_TEST_CASES=(
  ["missing_image"]="--output-file $OUTPUT_DIR/missing_image.txt"
  ["nonexistent_image"]="--image $IMAGE_PATH_NONEXISTENT --output-file $OUTPUT_DIR/nonexistent_image.txt"
  ["unsupported_format"]="--image $IMAGE_PATH_UNSUPPORTED --output-file $OUTPUT_DIR/unsupported_format.txt"
  ["invalid_table"]="--image $IMAGE_PATH --table=invalid --output-file $OUTPUT_DIR/invalid_table.txt"
  ["multiple_tables"]="--image $IMAGE_PATH --table=default --table=custom \".:-=+*#%@\" --output-file $OUTPUT_DIR/multiple_tables.txt"
  ["missing_bordered_chars"]="--image $IMAGE_PATH --table=bordered --output-file $OUTPUT_DIR/missing_bordered_chars.txt"
  ["invalid_bordered_borders"]="--image $IMAGE_PATH --table=bordered \".:-=+*#%@\" \"[abc,1,1,1,1]\" --output-file $OUTPUT_DIR/invalid_bordered_borders.txt"
  ["missing_brightness"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/missing_brightness.txt --brightness"
  ["invalid_brightness"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/invalid_brightness.txt --brightness notanumber"
  ["missing_rotation"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/missing_rotation.txt --rotate"
  ["invalid_rotation"]="--image $IMAGE_PATH --output-file $OUTPUT_DIR/invalid_rotation.txt --rotate notanumber"
  ["conflicting_image_and_random"]="--image $IMAGE_PATH --image-random --output-file $OUTPUT_DIR/conflicting_image_and_random.txt"
  ["missing_custom_table_chars"]="--image $IMAGE_PATH --table=custom --output-file $OUTPUT_DIR/missing_custom_table_chars.txt"
  ["non_numeric_brightness"]="--image $IMAGE_PATH --brightness abc --output-file $OUTPUT_DIR/non_numeric_brightness.txt"
  ["non_numeric_rotation"]="--image $IMAGE_PATH --rotate abc --output-file $OUTPUT_DIR/non_numeric_rotation.txt"
)

for test_name in "${!NEGATIVE_TEST_CASES[@]}"; do
  echo "Running negative test case: $test_name"

  # Construct the sbt command in batch mode
  CMD="run ${NEGATIVE_TEST_CASES[$test_name]}"
  echo "Executing: sbt -batch \"$CMD\""

  # Execute the sbt command in batch mode, capturing both stdout and stderr
  sbt_output=$(sbt -batch "$CMD" 2>&1)

  # Execute the sbt command in batch mode, capturing both stdout and stderr
  sbt_output=$(sbt -batch "$CMD" 2>&1)

  # Check if the output contains an error message
  if grep -q "^Error:" <<< "$sbt_output"; then
    # Print the success message in green since an error was expected and found
    echo -e "\e[32mNegative test case $test_name: PASSED - Expected error encountered.\e[0m"
    echo -e "\e[33mCaptured Error Output:\n$(grep '^Error:' <<< "$sbt_output")\e[0m"
  else
    # Print the failure message in red since no error was found
    echo -e "\e[31mNegative test case $test_name: FAILED - No error encountered.\e[0m"
    echo -e "\e[33mCaptured Output:\n$sbt_output\e[0m"
  fi
done