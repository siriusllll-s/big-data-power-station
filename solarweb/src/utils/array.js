export function findByProp (arr, prop, value) {
  if (!arr || !arr.length) return null
  for (let i = 0; i < arr.length; i++) {
    if (arr[i][prop] === value) return arr[i]
  }
  return null
}
