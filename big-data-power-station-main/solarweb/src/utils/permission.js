export function hasPermission (perms) {
  let hasPermission = false
  let permission = localStorage.getItem('userAuth')
  if (!permission) return false
  let index = permission.indexOf(perms)
  if (index > 0 || index === 0) {
    hasPermission = true
  }
  return hasPermission
}
