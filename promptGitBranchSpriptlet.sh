preexec_invoke_exec () {
    local temp=`git rev-parse --abbrev-ref HEAD 2>/dev/null`
    if [ -z $temp ]; then
        export PS1="\w$"
    else
        export PS1="\w[$temp]$"
    fi
}
export -f preexec_invoke_exec
export PROMPT_COMMAND='preexec_invoke_exec'
